/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;
import java.util.function.Supplier;

import io.github.mmm.bean.impl.BeanClassCreator;
import io.github.mmm.bean.impl.alias.AbstractBeanAliasMap;
import io.github.mmm.bean.impl.alias.BeanAliasMapEmpty;
import io.github.mmm.bean.impl.properties.BeanProperties;
import io.github.mmm.bean.impl.properties.BeanPropertiesFactory;
import io.github.mmm.bean.impl.properties.BeanPropertiesReadOnly;
import io.github.mmm.property.PropertyLock;
import io.github.mmm.property.PropertyMetadata;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.builder.PropertyBuilders;
import io.github.mmm.property.factory.PropertyFactoryManager;
import io.github.mmm.value.ReadablePath;

/// Abstract base implementation of [WritableBean].
/// **ATTENTION:**
/// If you plan to implement beans as Java class instead of using interfaces, you always need to have a public
/// constructor with [WritableBean] as a single argument. Otherwise [read-only][#getReadOnly()] support can
/// not work.
///
/// @see Bean
public abstract class AbstractBean implements WritableBean {

  private /* final */ BeanProperties properties;

  private AbstractBeanAliasMap aliases;

  private transient String pathSegment;

  private transient ReadablePath parentPath;

  private AbstractBean readOnly;

  private PropertyBuilders builders;

  /// The constructor.
  ///
  /// @param writable the [WritableBean] to wrap as [read-only][#isReadOnly()] bean or `null` to create
  ///        a mutable bean.
  public AbstractBean(WritableBean writable) {

    super();
    if (writable != null) {
      this.properties = new BeanPropertiesReadOnly(writable);
      this.readOnly = this;
    }
    this.aliases = BeanAliasMapEmpty.INSTANCE;
  }

  @Override
  public String pathSegment() {

    return this.pathSegment;
  }

  @Override
  public void pathSegment(String path) {

    requireWritable();
    this.pathSegment = path;
  }

  @Override
  public ReadablePath parentPath() {

    return this.parentPath;
  }

  @Override
  public void parentPath(ReadablePath parent) {

    requireWritable();
    this.parentPath = parent;
  }

  @Override
  public String path(boolean fixed) {

    if (fixed) {
      ReadablePath parent = parentPath();
      if (parent == null) {
        return getType().getSimpleName();
      }
      PathBuilder builder = new QualifiedNamePathBuilder();
      path(builder);
      return builder.toString();
    }
    return WritableBean.super.path(fixed);
  }

  /// @return `true` if the [Bean] will be access concurrently and is required to be thread-safe (use
  ///         concurrent collections, etc.), `false` otherwise. Please note we strongly encourage you not use
  ///         [Bean]s concurrently and overriding this method to return `true`. We also will not thoroughly
  ///         test such concurrency and just support it as best-guess implementation since it is relatively easy for us
  ///         but rather impossible to solve from the outside. So in case you are blocked by a concurrency issue that you
  ///         do not want to solve otherwise, you may consider overriding this method to return `true` for your
  ///         beans. Please also be warned that this causes quite some overhead and will reduce performance.
  protected boolean isThreadSafe() {

    return false;
  }

  @Override
  public boolean isDynamic() {

    return false;
  }

  /// Verifies that this [Bean] is writable (not [read-only][#isReadOnly()]).
  protected void requireWritable() {

    if (isReadOnly()) {
      throw new IllegalStateException("ReadOnly: " + toString());
    }
  }

  /// Verifies that this [Bean] is [dynamic][#isDynamic()] (extensible).
  protected void requireDynamic() {

    if (!isDynamic()) {
      throw new IllegalStateException("Not dynamic: " + toString());
    }
  }

  private BeanProperties getBeanProperties() {

    if (this.properties == null) {
      BeanPropertiesFactory factory = (BeanPropertiesFactory) getType();
      this.properties = factory.create(this);
    }
    return this.properties;
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public AbstractBean newInstance() {

    AbstractBean instance = create(null);
    if (isDynamic()) {
      // copy dynamic properties
      for (WritableProperty property : getBeanProperties().get()) {
        if (instance.getProperty(property.getName()) == null) {
          property = instance.copyProperty(property);
          instance.addProperty(property);
        }
      }
      instance.aliases = this.aliases;
    }
    return instance;
  }

  @Override
  public WritableBean copy() {

    AbstractBean copy = newInstance();
    BeanHelper.copy(this, copy);
    return copy;
  }

  // void makeReadOnly() {
  //
  // assert (!this.readOnly);
  // this.readOnly = true;
  // }

  @Override
  public final boolean isReadOnly() {

    return (this.readOnly == this);
  }

  /// @param lock the [lock][PropertyMetadata#getLock()] to check.
  /// @return `true` if this bean is the lock owner, `false` otherwise.
  protected boolean isLockOwnerInternal(PropertyLock lock) {

    return (lock == this);
  }

  @Override
  public final AbstractBean getReadOnly() {

    if (this.readOnly == null) {
      this.readOnly = create(this);
    }
    return this.readOnly;
  }

  @Override
  public BeanLock createReadOnlyLock() {

    if (this.readOnly != null) {
      return this.readOnly;
    }
    return new ReadOnlyBeanLock();
  }

  /// Internal method for magic trick to get real read-only lock.
  ///
  /// @return the [read-only bean][#getReadOnly()] view.
  protected BeanLock getReadOnlyOuterLock() {

    return getReadOnly();
  }

  /// Creates a new instance of this [Bean] implementation. The default implementation uses reflection. To improve
  /// performance please override this method. Please note, that if you do so, you also need to override this method
  /// again for all sub-classes of the hierarchy.
  ///
  /// @param writable the [AdvancedBean] to wrap as [read-only][#isReadOnly()] bean or `null` to create
  ///        a mutable bean.
  /// @return the new [Bean] instance. Has to be of the same type as the [current class][#getClass()].
  protected AbstractBean create(WritableBean writable) {

    try {
      return BeanClassCreator.doCreate(getClass(), writable);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public Collection<? extends WritableProperty<?>> getProperties() {

    return getBeanProperties().get();
  }

  @Override
  public WritableProperty<?> getProperty(String name) {

    // TODO move aliases to properties to avoid duplicate toLowercase overhead
    WritableProperty<?> property = getBeanProperties().get(name);
    if (property == null) {
      String resolvedAlias = this.aliases.getName(name);
      if (resolvedAlias != null) {
        property = getBeanProperties().get(resolvedAlias);
      }
    }
    return property;
  }

  @Override
  public int getPropertyCount() {

    return getBeanProperties().get().size();
  }

  @Override
  public <V> WritableProperty<V> createProperty(String name, Class<V> valueClass) {

    requireWritable();
    requireDynamic();
    PropertyMetadata<V> metadata = PropertyMetadata.of(this, null, null);
    WritableProperty<V> property = PropertyFactoryManager.get().create(valueClass, name, metadata);
    property = add(property);
    return property;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public <P extends WritableProperty<?>> P addProperty(P property) {

    requireWritable();
    requireDynamic();
    return (P) add((WritableProperty) property, AddMode.NORMAL);
  }

  /// Internal method for [#addProperty(WritableProperty)], without verification. Will be called from constructor
  /// of bean class implementations to register properties.
  ///
  /// @param <V> type of the [property value][WritableProperty#get()].
  /// @param <P> type of the [WritableProperty] to add.
  /// @param property the [WritableProperty] to add.
  /// @return the given `property`.
  protected <V, P extends WritableProperty<V>> P add(P property) {

    return add(property, AddMode.INTERNAL);
  }

  /// Internal method for [#addProperty(WritableProperty)], without verification. Shall only be used internally.
  /// From outside only use indirectly via [#addProperty(WritableProperty)] or from constructor via
  /// [#add(WritableProperty)].
  ///
  /// @param <P> type of the [WritableProperty] to add.
  /// @param property the [WritableProperty] to add.
  /// @param mode the [AddMode].
  /// @return the given `property`.
  @SuppressWarnings("unchecked")
  <V, P extends WritableProperty<V>> P add(P property, AddMode mode) {

    if (this.readOnly == this) {
      return (P) this.properties.get(property.getName());
    }
    if ((mode != AddMode.DIRECT)) {
      PropertyMetadata<V> metadata = property.getMetadata();
      if (!isLockOwnerInternal(metadata.getLock())) {
        property = copyProperty(property);
      }
    }
    if (mode.isAddInstance()) {
      getBeanProperties().add(property);
      onPropertyAdded(property);
    } else {
      PropertyFunction<P> function = new PropertyFunction<>(property, mode);
      getBeanProperties().addIfAbsent(property.getName(), function);
      if (function.result != null) {
        property = function.result;
        onPropertyAdded(property);
      }
    }
    return property;
  }

  /// @return the builder factory to build properties to be added to this bean.
  protected PropertyBuilders add() {

    if (this.builders == null) {
      this.builders = createPropertyBuilders();
    }
    return this.builders;
  }

  /// Internal method that may be overridden to replace the [StandardPropertyBuilders] implementation.
  ///
  /// @return the [StandardPropertyBuilders] instance.
  protected StandardPropertyBuilders createPropertyBuilders() {

    return new StandardPropertyBuilders(this);
  }

  private <V, P extends WritableProperty<V>> P copyProperty(P property) {

    PropertyMetadata<V> metadata = property.getMetadata().withLock(this);
    return WritableProperty.copy(property, null, metadata);
  }

  /// @param property the [WritableProperty] that has been added.
  protected void onPropertyAdded(WritableProperty<?> property) {

  }

  /// @param propertyName the [property name][ReadableProperty#getName()].
  /// @param alias the [alias][BeanAliasMap#getAliases(String)] to add.
  protected void registerAlias(String propertyName, String alias) {

    assert (getBeanProperties().get(propertyName) != null);
    addAlias(propertyName, alias);
  }

  private void addAlias(String propertyName, String alias) {

    assert verifyAlias(alias);
    this.aliases = this.aliases.add(propertyName, alias);
  }

  private boolean verifyAlias(String alias) {

    WritableProperty<?> property = getBeanProperties().get(alias);
    if (property == null) {
      return true;
    } else if (!property.getName().equals(alias)) {
      return true;
    }
    throw new IllegalStateException("Illegal alias '" + alias + "' pointing to an existing property name!");
  }

  /// @param propertyName the [property name][ReadableProperty#getName()].
  /// @param propertyAliases the [aliases][BeanAliasMap#getAliases(String)] to add.
  protected void registerAliases(String propertyName, String... propertyAliases) {

    assert (getBeanProperties().get(propertyName) != null) : "No property with name '" + propertyName
        + "' found to map by aliases " + Arrays.toString(propertyAliases);
    for (String alias : propertyAliases) {
      addAlias(propertyName, alias);
    }
  }

  @Override
  public BeanAliasMap getAliases() {

    return this.aliases;
  }

  @Override
  public String toString() {

    return doToString();
  }

  /// Internal method to get access to [#isThreadSafe()].
  ///
  /// @param bean the [AbstractBean].
  /// @return the value of [#isThreadSafe()].
  protected static boolean isThreadSafe(AbstractBean bean) {

    return bean.isThreadSafe();
  }

  /// Enum with the available modes for [adding a property internally][AbstractBean#add(WritableProperty, AddMode)].
  static enum AddMode {

    /// Add property from [AbstractBean#addProperty(WritableProperty)].
    NORMAL,

    /// Add property from [AbstractBean#add(WritableProperty)].
    INTERNAL,

    /// Add property from [PropertyBuilders].
    DIRECT,

    /// Add a [copy][WritableProperty#copy(WritableProperty)] of the property if none exists with that name.
    COPY,

    /// Add a [copy][WritableProperty#copy(WritableProperty)] of the property with the [value][WritableProperty#get()] if none exists with that name.
    COPY_WITH_VALUE,

    /// Add a [read-only view][WritableProperty#getReadOnly()] of the property if none exists with that name.
    READ_ONLY;

    /// @return `true` to add the given [WritableProperty] instance as-is, `false` otherwise (if a copy
    ///         or read-only view shall be added instead).
    public boolean isAddInstance() {

      return ((this == NORMAL) || (this == INTERNAL)) || (this == DIRECT);
    }
  }

  private class PropertyFunction<P extends WritableProperty<?>> implements Function<String, WritableProperty<?>> {

    private final P property;

    private final AddMode mode;

    private P result;

    private PropertyFunction(P property, AddMode mode) {

      super();
      this.property = property;
      this.mode = mode;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public WritableProperty<?> apply(String t) {

      if (this.mode == AddMode.READ_ONLY) {
        this.result = WritableProperty.getReadOnly(this.property);
      } else {
        this.result = (P) copyProperty((WritableProperty) this.property);
        if (this.mode == AddMode.COPY_WITH_VALUE) {
          if (!this.result.isReadOnly()) {
            ((WritableProperty) this.result).set(this.property.get());
          }
        }
      }
      return this.result;
    }
  }

  private static class QualifiedNamePathBuilder implements PathBuilder {

    private final StringBuilder buffer = new StringBuilder();

    @Override
    public void add(ReadablePath path) {

      String segment = null;
      if (path instanceof AbstractBean) {
        segment = ((AbstractBean) path).getType().getSimpleName();
      } else {
        segment = path.pathSegment();
      }
      add(segment);
    }

    @Override
    public void add(String segment) {

      if ((segment == null) || segment.isEmpty()) {
        return;
      }
      if (this.buffer.length() > 0) {
        this.buffer.append('.');
      }
      assert ((segment != null) && !segment.isEmpty());
      this.buffer.append(segment);
    }

    @Override
    public String toString() {

      return this.buffer.toString();
    }
  }

  private class ReadOnlyBeanLock implements BeanLock, Supplier<PropertyLock> {

    @Override
    public PropertyLock get() {

      return getReadOnlyOuterLock();
    }

    @Override
    public PropertyLock createReadOnlyLock() {

      return this;
    }

    @Override
    public boolean isReadOnly() {

      return true;
    }

    @Override
    public BeanType getType() {

      return AbstractBean.this.getType();
    }

  }

}
