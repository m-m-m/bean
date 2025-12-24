/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.util.Arrays;
import java.util.Collection;
import java.util.function.Function;

import io.github.mmm.bean.impl.BeanClassCreator;
import io.github.mmm.bean.impl.alias.AbstractBeanAliasMap;
import io.github.mmm.bean.impl.alias.BeanAliasMapEmpty;
import io.github.mmm.bean.impl.properties.BeanProperties;
import io.github.mmm.bean.impl.properties.BeanPropertiesFactory;
import io.github.mmm.bean.impl.properties.BeanPropertiesReadOnly;
import io.github.mmm.property.AttributeReadOnly;
import io.github.mmm.property.PropertyMetadata;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.builder.PropertyBuilders;
import io.github.mmm.property.factory.PropertyFactoryManager;
import io.github.mmm.value.ReadablePath;

/**
 * Abstract base implementation of {@link WritableBean}.<br>
 * <b>ATTENTION:</b><br>
 * If you plan to implement beans as Java class instead of using interfaces, you always need to have a public
 * constructor with {@link WritableBean} as a single argument. Otherwise {@link #getReadOnly() read-only} support can
 * not work.
 *
 * @see Bean
 */
public abstract class AbstractBean implements WritableBean {

  private /* final */ BeanProperties properties;

  private AbstractBeanAliasMap aliases;

  private transient String pathSegment;

  private transient ReadablePath parentPath;

  private AbstractBean readOnly;

  private PropertyBuilders builders;

  /**
   * The constructor.
   *
   * @param writable the {@link WritableBean} to wrap as {@link #isReadOnly() read-only} bean or {@code null} to create
   *        a mutable bean.
   */
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

  /**
   * @return {@code true} if the {@link Bean} will be access concurrently and is required to be thread-safe (use
   *         concurrent collections, etc.), {@code false} otherwise. Please note we strongly encourage you not use
   *         {@link Bean}s concurrently and overriding this method to return {@code true}. We also will not thoroughly
   *         test such concurrency and just support it as best-guess implementation since it is relatively easy for us
   *         but rather impossible to solve from the outside. So in case you are blocked by a concurrency issue that you
   *         do not want to solve otherwise, you may consider overriding this method to return {@code true} for your
   *         beans. Please also be warned that this causes quite some overhead and will reduce performance.
   */
  protected boolean isThreadSafe() {

    return false;
  }

  @Override
  public boolean isDynamic() {

    return false;
  }

  /**
   * Verifies that this {@link Bean} is writable (not {@link #isReadOnly() read-only}).
   */
  protected void requireWritable() {

    if (isReadOnly()) {
      throw new IllegalStateException("ReadOnly: " + toString());
    }
  }

  /**
   * Verifies that this {@link Bean} is {@link #isDynamic() dynamic} (extensible).
   */
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

  @Override
  public final AbstractBean getReadOnly() {

    if (this.readOnly == null) {
      this.readOnly = create(this);
    }
    return this.readOnly;
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

  /**
   * Creates a new instance of this {@link Bean} implementation. The default implementation uses reflection. To improve
   * performance please override this method. Please note, that if you do so, you also need to override this method
   * again for all sub-classes of the hierarchy.
   *
   * @param writable the {@link AdvancedBean} to wrap as {@link #isReadOnly() read-only} bean or {@code null} to create
   *        a mutable bean.
   * @return the new {@link Bean} instance. Has to be of the same type as the {@link #getClass() current class}.
   */
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

  /**
   * Internal method for {@link #addProperty(WritableProperty)}, without verification. Will be called from constructor
   * of bean class implementations to register properties.
   *
   * @param <V> type of the {@link WritableProperty#get() property value}.
   * @param <P> type of the {@link WritableProperty} to add.
   * @param property the {@link WritableProperty} to add.
   * @return the given {@code property}.
   */
  protected <V, P extends WritableProperty<V>> P add(P property) {

    return add(property, AddMode.INTERNAL);
  }

  /**
   * Internal method for {@link #addProperty(WritableProperty)}, without verification. Shall only be used internally.
   * From outside only use indirectly via {@link #addProperty(WritableProperty)} or from constructor via
   * {@link #add(WritableProperty)}.
   *
   * @param <P> type of the {@link WritableProperty} to add.
   * @param property the {@link WritableProperty} to add.
   * @param mode the {@link AddMode}.
   * @return the given {@code property}.
   */
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

  /**
   * @param lock the {@link PropertyMetadata#getLock() lock} to check.
   * @return {@code true} if this bean is the lock owner, {@code false} otherwise.
   */
  protected boolean isLockOwnerInternal(AttributeReadOnly lock) {

    return (lock == this);
  }

  /**
   * @return the builder factory to build properties to be added to this bean.
   */
  protected PropertyBuilders add() {

    if (this.builders == null) {
      this.builders = createPropertyBuilders();
    }
    return this.builders;
  }

  /**
   * Internal method that may be overridden to replace the {@link StandardPropertyBuilders} implementation.
   *
   * @return the {@link StandardPropertyBuilders} instance.
   */
  protected StandardPropertyBuilders createPropertyBuilders() {

    return new StandardPropertyBuilders(this);
  }

  private <V, P extends WritableProperty<V>> P copyProperty(P property) {

    PropertyMetadata<V> metadata = property.getMetadata().withLock(this);
    return WritableProperty.copy(property, null, metadata);
  }

  /**
   * @param property the {@link WritableProperty} that has been added.
   */
  protected void onPropertyAdded(WritableProperty<?> property) {

  }

  /**
   * @param propertyName the {@link ReadableProperty#getName() property name}.
   * @param alias the {@link BeanAliasMap#getAliases(String) alias} to add.
   */
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

  /**
   * @param propertyName the {@link ReadableProperty#getName() property name}.
   * @param propertyAliases the {@link BeanAliasMap#getAliases(String) aliases} to add.
   */
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

  /**
   * Internal method to get access to {@link #isThreadSafe()}.
   *
   * @param bean the {@link AbstractBean}.
   * @return the value of {@link #isThreadSafe()}.
   */
  protected static boolean isThreadSafe(AbstractBean bean) {

    return bean.isThreadSafe();
  }

  /**
   * Enum with the available modes for {@link AbstractBean#add(WritableProperty, AddMode) adding a property internally}.
   */
  static enum AddMode {

    /** Add property from {@link AbstractBean#addProperty(WritableProperty)}. */
    NORMAL,

    /** Add property from {@link AbstractBean#add(WritableProperty)}. */
    INTERNAL,

    /** Add property from {@link PropertyBuilders}. */
    DIRECT,

    /** Add a {@link WritableProperty#copy(WritableProperty) copy} of the property if none exists with that name. */
    COPY,

    /**
     * Add a {@link WritableProperty#copy(WritableProperty) copy} of the property with the {@link WritableProperty#get()
     * value} if none exists with that name.
     */
    COPY_WITH_VALUE,

    /** Add a {@link WritableProperty#getReadOnly() read-only view} of the property if none exists with that name. */
    READ_ONLY;

    /**
     * @return {@code true} to add the given {@link WritableProperty} instance as-is, {@code false} otherwise (if a copy
     *         or read-only view shall be added instead).
     */
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

}
