/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import net.sf.mmm.property.WritableProperty;
import net.sf.mmm.property.factory.PropertyFactoryManager;
import net.sf.mmm.property.factory.PropertyFactoryManagerImpl;

/**
 * Abstract base implementation of {@link WritableBean}.
 *
 * @see Bean
 */
public abstract class AbstractBean implements WritableBean {

  private static final Class<?>[] SIGNATURE = new Class[] { Bean.class, boolean.class };

  private final Map<String, WritableProperty<?>> propertiesMap;

  private final Collection<WritableProperty<?>> properties;

  private final boolean dynamic;

  private final AbstractBean writable;

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
      this.propertiesMap = new ConcurrentHashMap<>();
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
      Constructor<? extends AbstractBean> constructor = getClass().getConstructor(SIGNATURE);
      return constructor.newInstance(writableBean, Boolean.valueOf(dynamicFlag));
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
        property = add(property);
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
    PropertyFactoryManager factoryManager = PropertyFactoryManagerImpl.getInstance();
    boolean polymorphic = false;
    WritableProperty<V> property = factoryManager.create(valueClass, polymorphic, name);
    add(property);
    return property;
  }

  @Override
  public <P extends WritableProperty<?>> P addProperty(P property) {

    requireWritable();
    requireDynamic();
    return add(property);
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

    return add(property, AddMode.NORMAL);
  }

  /**
   * Internal method for {@link #addProperty(WritableProperty)}, without verification. Will be called from constructor
   * of bean class implementations to register properties.
   *
   * @param <P> type of the {@link WritableProperty} to add.
   * @param property the {@link WritableProperty} to add.
   * @param mode the {@link AddMode}.
   * @return the given {@code property}.
   */
  @SuppressWarnings("unchecked")
  protected <P extends WritableProperty<?>> P add(P property, AddMode mode) {

    if (this.writable != null) {
      property = (P) this.writable.getProperty(property.getName()).getReadOnly();
    }
    WritableProperty<?> existing;
    switch (mode) {
      case NORMAL:
        existing = this.propertiesMap.putIfAbsent(property.getName(), property);
        if (existing != null) {
          throw new IllegalArgumentException("Duplicate property " + property.getName());
        }
        onPropertyAdded(property);
        break;
      case COPY:
        CopyFunction<P> copyFunction = new CopyFunction<>(property);
        existing = this.propertiesMap.computeIfAbsent(property.getName(), copyFunction);
        if (copyFunction.copy != null) {
          property = copyFunction.copy;
          onPropertyAdded(property);
        }
        break;
      case READ_ONLY:
        ReadOnlyFunction<P> readOnlyFunction = new ReadOnlyFunction<>(property);
        existing = this.propertiesMap.computeIfAbsent(property.getName(), readOnlyFunction);
        if (readOnlyFunction.readOnly != null) {
          property = readOnlyFunction.readOnly;
          onPropertyAdded(property);
        }
        break;
    }
    return property;
  }

  /**
   * @param property the {@link WritableProperty} that has been added.
   */
  protected void onPropertyAdded(WritableProperty<?> property) {

  }

  @Override
  public String toString() {

    StringBuilder sb = new StringBuilder(getType().getStableName());
    sb.append("[");
    if (isReadOnly()) {
      sb.append("readonly,");
    }
    if (isDynamic()) {
      sb.append("dynamic,");
    }
    toString(sb);
    sb.append("]");
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
  protected static enum AddMode {

    /** Add property as-is. */
    NORMAL,

    /** Add a {@link WritableProperty#copy(WritableProperty) copy} of the property if none exists with that name. */
    COPY,

    /** Add a {@link WritableProperty#getReadOnly() read-only view} of the property if none exists with that name. */
    READ_ONLY
  }

  private static class CopyFunction<P extends WritableProperty<?>> implements Function<String, WritableProperty<?>> {

    private final P property;

    private P copy;

    private CopyFunction(P property) {

      super();
      this.property = property;
    }

    @Override
    public WritableProperty<?> apply(String t) {

      this.copy = WritableProperty.copy(this.property);
      return this.copy;
    }

  }

  private static class ReadOnlyFunction<P extends WritableProperty<?>>
      implements Function<String, WritableProperty<?>> {

    private final P property;

    private P readOnly;

    private ReadOnlyFunction(P property) {

      super();
      this.property = property;
    }

    @Override
    public WritableProperty<?> apply(String t) {

      this.readOnly = WritableProperty.getReadOnly(this.property);
      return this.readOnly;
    }

  }

}
