/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.github.mmm.bean.impl.BeanCreator;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.builder.DefaultPropertyBuilders;
import io.github.mmm.property.factory.PropertyFactoryManager;

/**
 * Abstract base implementation of {@link WritableBean}.
 *
 * @see Bean
 */
public abstract class AbstractBean implements WritableBean {

  private final Map<String, WritableProperty<?>> propertiesMap;

  private final Collection<WritableProperty<?>> properties;

  private final boolean dynamic;

  final AbstractBean writable;

  private DefaultPropertyBuilders builders;

  private AbstractBean readOnly;

  /**
   * The constructor.
   *
   * @param writable the writable {@link Bean} to create a {@link #isReadOnly() read-only} view on or {@code null} to
   *        create a regular mutable {@link Bean}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   */
  public AbstractBean(AbstractBean writable, boolean dynamic) {

    super();
    this.writable = writable;
    if (writable != null) {
      assert (dynamic == writable.isDynamic());
      this.readOnly = this;
    }
    this.dynamic = dynamic;
    if (isThreadSafe()) {
      // temporary workaround for https://github.com/konsoletyper/teavm/issues/445
      // this.propertiesMap = new ConcurrentHashMap<>();
      this.propertiesMap = new HashMap<>();
    } else {
      this.propertiesMap = new HashMap<>();
    }
    this.properties = Collections.unmodifiableCollection(this.propertiesMap.values());
  }

  /**
   * @return {@code true} if the {@link Bean} shall be thread-safe (use concurrent collections, etc.), {@code false}
   *         otherwise.
   */
  protected boolean isThreadSafe() {

    return false;
  }

  @Override
  public boolean isDynamic() {

    return this.dynamic;
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

  @Override
  public WritableBean newInstance() {

    AbstractBean instance = create(null, this.dynamic);
    if (this.dynamic) {
      // copy dynamic properties
      for (WritableProperty<?> property : this.properties) {
        if (instance.getProperty(property.getName()) == null) {
          instance.addProperty(WritableProperty.copy(property));
        }
      }
    }
    return instance;
  }

  @Override
  public final boolean isReadOnly() {

    return (this.writable != null);
  }

  @Override
  public final AbstractBean getReadOnly() {

    if (this.readOnly == null) {
      this.readOnly = create(this, isDynamic());
    }
    return this.readOnly;
  }

  /**
   * Creates a new instance of this {@link Bean} implementation. The default implementation uses reflection. To improve
   * performance please override this method. Please note, that if you do so, you also need to override this method
   * again for all sub-classes of the hierarchy.
   *
   * @param writableBean the writable {@link Bean} to create a {@link #isReadOnly() read-only} view on, or {@code null}
   *        to create a regular mutable {@link Bean}.
   * @param dynamicFlag the {@link #isDynamic() dynamic flag}.
   * @return the new {@link Bean} instance. Has to be of the same type as the {@link #getClass() current class}.
   */
  protected AbstractBean create(AbstractBean writableBean, boolean dynamicFlag) {

    try {
      return BeanCreator.create(getClass(), writableBean, dynamicFlag);
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public Iterable<? extends WritableProperty<?>> getProperties() {

    if (this.writable != null) {
      for (WritableProperty<?> property : this.writable.getProperties()) {
        add(property, AddMode.READ_ONLY);
      }
    } else if (this.dynamic) {
      updateProperties();
    }
    return this.properties;
  }

  @Override
  public WritableProperty<?> getProperty(String name) {

    if ((this.writable == null) && this.dynamic) {
      updateProperties();
    }
    String resolvedAlias = getPropertyNameForAlias(name);
    if (resolvedAlias != null) {
      name = resolvedAlias;
    }
    WritableProperty<?> property = this.propertiesMap.get(name);
    if ((property == null) && (this.writable != null)) {
      property = this.writable.getProperty(name);
      if (property != null) {
        property = add(property.getReadOnly());
      }
    }
    return property;
  }

  @Override
  public int getPropertyCount() {

    if (this.writable != null) {
      return this.writable.getPropertyCount();
    }
    if (this.dynamic) {
      updateProperties();
    }
    return this.propertiesMap.size();
  }

  /**
   * Called before properties are accessed if {@link #isDynamic() dynamic} and not {@link #isReadOnly() read-only} to
   * allow implementations to update properties internally before.
   */
  protected void updateProperties() {

  }

  @Override
  public <V> WritableProperty<V> createProperty(String name, Class<V> valueClass, Type valueType) {

    requireWritable();
    requireDynamic();
    WritableProperty<V> property = PropertyFactoryManager.get().create(valueClass, name);
    add(property);
    return property;
  }

  @Override
  public <P extends WritableProperty<?>> P addProperty(P property) {

    requireWritable();
    requireDynamic();
    return add(property, AddMode.NORMAL);
  }

  /**
   * Internal method for {@link #addProperty(WritableProperty)}, without verification. Will be called from constructor
   * of bean class implementations to register properties.
   *
   * @param <P> type of the {@link WritableProperty} to add.
   * @param property the {@link WritableProperty} to add.
   * @return the given {@code property}.
   */
  protected <P extends WritableProperty<?>> P add(P property) {

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
  <P extends WritableProperty<?>> P add(P property, AddMode mode) {

    if ((this.writable != null) && (mode != AddMode.DIRECT)) {
      property = (P) this.writable.getProperty(property.getName()).getReadOnly();
    }
    WritableProperty<?> existing;
    if (mode.isAddInstance()) {
      existing = this.propertiesMap.putIfAbsent(property.getName(), property);
      if (existing != null) {
        throw new IllegalArgumentException("Duplicate property " + property.getName());
      }
      onPropertyAdded(property);
    } else {
      PropertyFunction<P> function = new PropertyFunction<>(property, mode);
      this.propertiesMap.computeIfAbsent(property.getName(), function);
      if (function.result != null) {
        property = function.result;
        onPropertyAdded(property);
      }
    }
    return property;
  }

  /**
   * @return the builder factory to build properties to be added to this bean.
   */
  protected DefaultPropertyBuilders add() {

    if (this.builders == null) {
      this.builders = createPropertyBuilders();
    }
    return this.builders;
  }

  /**
   * Internal method that may be overridden to replace the {@link PropertyBuilders} implementation.
   *
   * @return the {@link PropertyBuilders} instance.
   */
  protected PropertyBuilders createPropertyBuilders() {

    return new PropertyBuilders(this);
  }

  /**
   * @param property the {@link WritableProperty} that has been added.
   */
  protected void onPropertyAdded(WritableProperty<?> property) {

  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder(getType().getStableName());
    sb.append("(");
    if (isReadOnly()) {
      sb.append("readonly,");
    } else {
      sb.append("mutable,");
    }
    if (isDynamic()) {
      sb.append("dynamic,");
    }
    toString(sb);
    sb.setLength(sb.length() - 1);
    sb.append(")");
    return sb.toString();
  }

  /**
   * @param sb the {@link StringBuilder} where to append the details (the properties) of this {@link Bean} for
   *        {@link #toString()}-Representation.
   */
  public void toString(StringBuilder sb) {

    for (WritableProperty<?> property : getProperties()) {
      property.toString(sb);
      sb.append(",");
    }
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

  private static class PropertyFunction<P extends WritableProperty<?>>
      implements Function<String, WritableProperty<?>> {

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
        this.result = WritableProperty.copy(this.property);
        if (this.mode == AddMode.COPY_WITH_VALUE) {
          if (!this.result.isReadOnly()) {
            ((WritableProperty) this.result).set(this.property.get());
          }
        }
      }
      return this.result;
    }
  }

}
