/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.marshall.MarshallingObject;
import io.github.mmm.marshall.StructuredReader;
import io.github.mmm.marshall.StructuredState;
import io.github.mmm.marshall.StructuredWriter;
import io.github.mmm.marshall.id.StructuredIdMapping;
import io.github.mmm.marshall.id.StructuredIdMappingObject;
import io.github.mmm.property.AttributeReadOnly;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.value.WritablePath;

/**
 * Writable interface of {@link ReadableBean}.
 */
@AbstractInterface
public interface WritableBean extends ReadableBean, WritablePath, MarshallingObject, StructuredIdMappingObject {

  @Override
  WritableProperty<?> getProperty(String name);

  @Override
  Collection<? extends WritableProperty<?>> getProperties();

  @Override
  default WritableProperty<?> getRequiredProperty(String name) {

    WritableProperty<?> property = getProperty(name);
    if (property == null) {
      throw new ObjectNotFoundException("Property@" + getJavaClass().getName(), name);
    }
    return property;
  }

  /**
   * Sets the value of the {@link #getRequiredProperty(String) required property} with the given {@code name} to the
   * given {@code value}.
   *
   * @param name the {@link WritableProperty#getName() name} of the property.
   * @param value new {@link WritableProperty#get() value} of the specified property.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  default void set(String name, Object value) {

    WritableProperty property = getRequiredProperty(name);
    property.set(value);
  }

  /**
   * Sets the value of the {@link #getProperty(String) property} with the given {@code name} to the given {@code value}.
   * If no such {@link #getProperty(String) property} exists, it will be {@link #createProperty(String, Class) created}
   * {@link #isDynamic() dynamically} if the given value is not {@code null}.
   *
   * @param name the {@link WritableProperty#getName() name} of the property.
   * @param value new {@link WritableProperty#get() value} of the specified property.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  default void setDynamic(String name, Object value) {

    WritableProperty property = getProperty(name);
    if (property == null) {
      if (value == null) {
        return;
      }
      property = createProperty(name, value.getClass());
    }
    property.set(value);
  }

  /**
   * /** Sets the value of the {@link #getOrCreateProperty(String, Class) existing or newly created property} with the
   * given {@code name} to the given {@code value}.
   *
   * @param <V> the generic type of the {@link WritableProperty#getValueClass() value class}.
   * @param name the {@link WritableProperty#getName() property name}.
   * @param value new {@link WritableProperty#get() value} of the specified property.
   * @param valueClass the {@link WritableProperty#getValueClass() value class}.
   * @see #set(String, Object)
   */
  default <V> void set(String name, V value, Class<V> valueClass) {

    WritableProperty<V> property = getOrCreateProperty(name, valueClass);
    property.set(value);
  }

  /**
   * Adds the given {@link WritableProperty} to this bean.
   *
   * @param <P> type of the {@link WritableProperty} to add.
   * @param property the {@link WritableProperty} to add.
   * @return the given {@code property}.
   * @throws IllegalStateException if this {@link WritableBean} is {@link #isReadOnly() read-only} or not
   *         {@link #isDynamic() dynamic}.
   */
  <P extends WritableProperty<?>> P addProperty(P property);

  /**
   * Creates and {@link #addProperty(WritableProperty) adds} a {@link WritableProperty} with the given
   * {@link WritableProperty#getValueClass() value class} {@link #isDynamic() dynamically}.
   *
   * @param <V> the generic type of the {@link WritableProperty#getValueClass() value class}.
   * @param name the {@link WritableProperty#getName() property name}.
   * @param valueClass the {@link WritableProperty#getValueClass() value class}.
   * @return the newly created and added property.
   * @throws IllegalStateException if this {@link WritableBean} already has such property, is {@link #isReadOnly()
   *         read-only}, or not {@link #isDynamic() dynamic}.
   */
  <V> WritableProperty<V> createProperty(String name, Class<V> valueClass);

  /**
   * {@link #getProperty(String) Gets} or {@link #createProperty(String, Class) creates} the specified property
   * {@link #isDynamic() dynamically}.
   *
   * @param <V> the generic type of the {@link WritableProperty#get() property value}.
   * @param name the {@link WritableProperty#getName() property name}.
   * @param valueClass the {@link WritableProperty#getValueClass() value class} of the requested property.
   * @return the requested property. Will be created if it does not already {@link #getProperty(String) exist}.
   * @throws IllegalArgumentException if the requested property already exists but has an incompatible
   *         {@link WritableProperty#getValueClass() value class}.
   * @throws IllegalStateException if the requested property does not exist but this {@link WritableBean} is
   *         {@link #isReadOnly() read-only}, or not {@link #isDynamic() dynamic}.
   */
  @SuppressWarnings("unchecked")
  default <V> WritableProperty<V> getOrCreateProperty(String name, Class<V> valueClass) {

    WritableProperty<?> property = getProperty(name);
    if (property != null) {
      if (valueClass.isAssignableFrom(property.getValueClass())) {
        return ((WritableProperty<V>) property);
      } else {
        throw new IllegalArgumentException("Property '" + name + "' with value class "
            + property.getValueClass().getName() + " was requested for mismatching value class " + valueClass);
      }
    }
    return createProperty(name, valueClass);
  }

  @Override
  BeanType getType();

  /**
   * @return the {@link #isReadOnly() read only} view on this {@link WritableBean bean}.
   * @see #isReadOnly()
   * @see #getReadOnly(WritableBean)
   */
  WritableBean getReadOnly();

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  default WritableBean read(StructuredReader reader) {

    if (!reader.readStartObject(this) || reader.readEnd()) {
      return this;
    }
    WritableBean result = this;
    int propertyCount = 0;
    while (!reader.readEnd()) {
      String propertyName = reader.readName();
      if (StructuredReader.TYPE.equals(propertyName)) {
        String type = reader.readValueAsString();
        String stableName = getType().getStableName();
        if (propertyCount > 0) {
          propertyCount++; // 1-based index for human reader
          throw new IllegalStateException("Property " + StructuredReader.TYPE + " of " + getType()
              + " must come first but was " + propertyCount + ". property!");
        } else if (!type.equals(stableName)) {
          // TODO implement polymorphism
          // result = BeanFactory.get().create(stableName);
          throw new IllegalStateException(StructuredReader.TYPE + "=" + type + "!=" + stableName);
        }
      } else {
        WritableProperty<?> property = result.getProperty(propertyName);
        if (property == null) {
          if (isDynamic()) {
            Object value = reader.readValue(true);
            if (value != null) {
              Class<? extends Object> valueClass = value.getClass();
              property = createProperty(propertyName, valueClass);
              ((WritableProperty) property).set(value);
            }
          } else {
            // LOG.debug("ignoring undefined property {}.{}", getBeanClass(), propertyName);
            reader.skipValue();
          }
        } else {
          property.readObject(reader);
        }
      }
      propertyCount++;
    }
    return result;
  }

  @Override
  default void write(StructuredWriter writer) {

    writer.writeStartObject(this);
    if (isPolymorphic()) {
      writer.writeName(StructuredWriter.TYPE);
      writer.writeValueAsString(getType().getStableName());
    }
    for (ReadableProperty<?> property : getProperties()) {
      if (!property.isTransient()) {
        String propertyName = property.getName();
        writer.writeName(propertyName);
        property.writeObject(writer, property);
      }
    }
    writer.writeEnd();
  }

  @Override
  default StructuredIdMapping defineIdMapping() {

    // TODO: how to signal to use default mapping if not overridden and properly implemented?
    return null;
  }

  @Override
  default Object asTypeKey() {

    if (isDynamic()) {
      return getType();
    }
    return getType().getJavaClass();
  }

  /**
   * @return the optional alias of this bean. It will be appended as prefix to the {@link #path()} of all
   *         {@link #getProperties() properties}. This is useful for API to build queries. So e.g. set to "alias" and
   *         your contained property will have the path "alias.MyProperty".
   */
  @Override
  String pathSegment();

  /**
   * Used to set an alias as described in {@link #pathSegment()}.
   *
   * <b>ATTENTION:</b> End users should never use this method directly but use designated methods such as
   * {@code as(String alias)} provided by according SQL clauses.
   */
  @Override
  void pathSegment(String pathSegment);

  /**
   * @param <B> type of the {@link WritableBean}.
   * @param bean the {@link WritableBean} to get a {@link #isReadOnly() read-only} {@link #getReadOnly() view} of.
   * @return the {@link #isReadOnly() read-only} {@link #getReadOnly() view} of the given {@link WritableBean}.
   */
  @SuppressWarnings("unchecked")
  static <B extends WritableBean> B getReadOnly(B bean) {

    return (B) bean.getReadOnly();
  }

  /**
   * @param <B> type of the {@link WritableBean} owning the {@link WritableProperty property}.
   * @param property the {@link WritableProperty} for which the owning {@link WritableBean} is requested.
   * @return the {@link WritableBean} owning the given {@link WritableProperty property}. Will be {@code null} if the
   *         {@link WritableProperty} is manually created from outside a bean.
   */
  @SuppressWarnings("unchecked")
  static <B extends WritableBean> B from(WritableProperty<?> property) {

    AttributeReadOnly lock = property.getMetadata().getLock();
    if (lock instanceof WritableBean) {
      return (B) lock;
    }
    return null;
  }

  /**
   * @param <B> type of the {@link WritableBean} to read.
   * @param template an instance of the {@link WritableBean} to read acting as template.
   * @param reader the {@link StructuredReader} to read the data from.
   * @return the {@link List} with the the unmarshalled {@link WritableBean beans}.
   */
  static <B extends WritableBean> List<B> readArray(B template, StructuredReader reader) {

    List<B> list = new ArrayList<>();
    readArray(template, reader, list);
    return list;
  }

  /**
   * @param <B> type of the {@link WritableBean} to read.
   * @param template an instance of the {@link WritableBean} to read acting as template.
   * @param reader the {@link StructuredReader} to read the data from.
   * @param collection the {@link Collection} where to {@link Collection#add(Object) add} the unmarshalled
   *        {@link WritableBean beans}.
   */
  static <B extends WritableBean> void readArray(B template, StructuredReader reader, Collection<B> collection) {

    reader.require(StructuredState.START_ARRAY, true);
    while (!reader.readEndArray()) {
      B bean = ReadableBean.newInstance(template);
      bean.read(reader);
      collection.add(bean);
    }
  }

  /**
   * @param <B> type of the {@link WritableBean} to write.
   * @param beans the {@link Collection} with the {@link WritableBean beans} to write.
   * @param writer the {@link StructuredWriter} to write to.
   */
  static <B extends WritableBean> void writeArray(Collection<B> beans, StructuredWriter writer) {

    writer.writeStartArray();
    for (B bean : beans) {
      if (bean == null) {
        writer.writeValueAsNull();
      } else {
        bean.write(writer);
      }
    }
    writer.writeEnd();
  }

}
