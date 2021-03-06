/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import io.github.mmm.bean.impl.BeanCreator;
import io.github.mmm.bean.mapping.PropertyIdMapper;
import io.github.mmm.bean.mapping.PropertyIdMapping;
import io.github.mmm.marshall.StructuredBinaryFormat;
import io.github.mmm.marshall.StructuredFormat;
import io.github.mmm.marshall.StructuredWriter;
import io.github.mmm.marshall.size.StructuredFormatSizeComputor;
import io.github.mmm.marshall.size.StructuredFormatSizeComputorNone;
import io.github.mmm.property.AttributeReadOnly;
import io.github.mmm.property.PropertyMetadata;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.builder.PropertyBuilders;
import io.github.mmm.property.factory.PropertyFactoryManager;

/**
 * Abstract base implementation of {@link WritableBean}.
 *
 * @see Bean
 */
public abstract class AbstractBean implements WritableBean {

  private final Map<String, WritableProperty<?>> propertiesMap;

  private final Collection<WritableProperty<?>> properties;

  private transient Supplier<String> pathSupplier;

  private boolean readOnly;

  private PropertyBuilders builders;

  private int size;

  /**
   * The constructor.
   */
  public AbstractBean() {

    super();
    if (isThreadSafe()) {
      // temporary workaround for https://github.com/konsoletyper/teavm/issues/445
      // this.propertiesMap = new ConcurrentHashMap<>();
      this.propertiesMap = new HashMap<>();
    } else {
      this.propertiesMap = new HashMap<>();
    }
    this.properties = Collections.unmodifiableCollection(this.propertiesMap.values());
    this.size = -1;
  }

  @Override
  public String path() {

    if (this.pathSupplier == null) {
      return "";
    }
    return this.pathSupplier.get();
  }

  @Override
  public void path(String path) {

    path(() -> path);
  }

  @Override
  public void path(Supplier<String> pathExpression) {

    this.pathSupplier = pathExpression;
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

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public AbstractBean newInstance() {

    AbstractBean instance = create();
    if (isDynamic()) {
      // copy dynamic properties
      for (WritableProperty property : this.properties) {
        if (instance.getProperty(property.getName()) == null) {
          property = copyProperty(property);
          instance.addProperty(property);
        }
      }
    }
    return instance;
  }

  @Override
  public WritableBean copy(boolean isReadOnly) {

    if (this.readOnly && isReadOnly) {
      return this;
    }
    AbstractBean copy = newInstance();
    BeanHelper.copy(this, copy, isReadOnly);
    return copy;
  }

  void makeReadOnly() {

    assert (!this.readOnly);
    this.readOnly = true;
  }

  @Override
  public final boolean isReadOnly() {

    return this.readOnly;
  }

  /**
   * Creates a new instance of this {@link Bean} implementation. The default implementation uses reflection. To improve
   * performance please override this method. Please note, that if you do so, you also need to override this method
   * again for all sub-classes of the hierarchy.
   *
   * @return the new {@link Bean} instance. Has to be of the same type as the {@link #getClass() current class}.
   */
  protected AbstractBean create() {

    try {
      return BeanCreator.doCreate(getClass());
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public Collection<? extends WritableProperty<?>> getProperties() {

    return this.properties;
  }

  @Override
  public WritableProperty<?> getProperty(String name) {

    String resolvedAlias = getPropertyNameForAlias(name);
    if (resolvedAlias != null) {
      name = resolvedAlias;
    }
    WritableProperty<?> property = this.propertiesMap.get(name);
    return property;
  }

  @Override
  public int getPropertyCount() {

    return this.propertiesMap.size();
  }

  @Override
  public <V> WritableProperty<V> createProperty(String name, Class<V> valueClass, Type valueType) {

    requireWritable();
    requireDynamic();
    PropertyMetadata<V> metadata = PropertyMetadata.of(this, null, null, valueType);
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
  <V, P extends WritableProperty<V>> P add(P property, AddMode mode) {

    WritableProperty<?> existing;
    if ((mode != AddMode.DIRECT)) {
      PropertyMetadata<V> metadata = property.getMetadata();
      if (!isLockOwnerInternal(metadata.getLock())) {
        property = copyProperty(property);
      }
    }
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

  int computeSize(StructuredFormatSizeComputor computor, PropertyIdMapping idMapping) {

    if (this.size != -1) {
      int newSize = 0;
      for (ReadableProperty<?> property : getProperties()) {
        int propertySize = property.computeSize(computor);
        if (propertySize == -1) {
          return -1;
        } else if (propertySize > 0) {
          propertySize += computor.sizeOfProperty(property.getName(), idMapping.id(property));
        }
        newSize += propertySize;
      }
      this.size = newSize;
    }
    return this.size;
  }

  @Override
  public void write(StructuredWriter writer) {

    StructuredFormat format = writer.getFormat();
    PropertyIdMapping idMapping = null;
    if (format.isIdBased()) {
      idMapping = PropertyIdMapper.get().getIdMapping(this);
    }
    if (format.isBinary()) {
      StructuredFormatSizeComputor computor = ((StructuredBinaryFormat) format).getSizeComputor();
      if (computor != StructuredFormatSizeComputorNone.get()) {
        computeSize(computor, idMapping);
      }
    }
    writer.writeStartObject(this.size);
    if (isPolymorphic()) {
      writer.writeName(PROPERTY_TYPE_NAME, PROPERTY_TYPE_ID);
      writer.writeValueAsString(getType().getStableName());
    }
    for (ReadableProperty<?> property : getProperties()) {
      String propertyName = property.getName();
      int propertyId = -1;
      if (idMapping != null) {
        propertyId = idMapping.id(property);
      }
      if (!property.isTransient()) {
        writer.writeName(propertyName, propertyId);
        property.writeObject(writer, property);
      }
    }
    writer.writeEnd();
    this.size = -1;
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

}
