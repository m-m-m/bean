/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.lang.reflect.Type;
import java.util.Collection;

import io.github.mmm.bean.mapping.PropertyIdMapper;
import io.github.mmm.bean.mapping.PropertyIdMapping;
import io.github.mmm.marshall.MarshallingObject;
import io.github.mmm.marshall.StructuredReader;
import io.github.mmm.property.WritableProperty;

/**
 * Writable interface of {@link ReadableBean}.
 */
@AbstractInterface
public interface WritableBean extends ReadableBean, MarshallingObject {

  @Override
  WritableProperty<?> getProperty(String name);

  @Override
  Collection<? extends WritableProperty<?>> getProperties();

  @Override
  default WritableProperty<?> getRequiredProperty(String name) {

    WritableProperty<?> property = getProperty(name);
    if (property == null) {
      throw new IllegalArgumentException("Missing property: " + name);
    }
    return property;
  }

  /**
   * Sets the value of the {@link #getRequiredProperty(String) required property} with the given {@code name} to the
   * given value.
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
   * /** Sets the value of the {@link #getOrCreateProperty(String, Class) existing or newly created property} with the
   * given {@code name} to the given value.
   *
   * @param <V> the generic type of the {@link WritableProperty#getValueClass() value class}.
   * @param name the {@link WritableProperty#getName() property name}.
   * @param value new {@link WritableProperty#get() value} of the specified property.
   * @param valueClass the {@link WritableProperty#getValueClass() value class}.
   * @see #set(String, Object)
   */
  default <V> void set(String name, V value, Class<V> valueClass) {

    set(name, value, valueClass, valueClass);
  }

  /**
   * /** Sets the value of the {@link #getOrCreateProperty(String, Class, Type) existing or newly created property} with
   * the given {@code name} to the given value.
   *
   * @param <V> the generic type of the {@link WritableProperty#getValueClass() value class}.
   * @param name the {@link WritableProperty#getName() property name}.
   * @param value new {@link WritableProperty#get() value} of the specified property.
   * @param valueClass the {@link WritableProperty#getValueClass() value class}.
   * @param valueType the {@link Type} reflecting the {@link WritableProperty#get() property value}.
   * @see #set(String, Object)
   */
  default <V> void set(String name, V value, Class<V> valueClass, Type valueType) {

    WritableProperty<V> property = getOrCreateProperty(name, valueClass, valueType);
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
   * {@link WritableProperty#getValueClass() value class} dynamically.
   *
   * @param <V> the generic type of the {@link WritableProperty#getValueClass() value class}.
   * @param name the {@link WritableProperty#getName() property name}.
   * @param valueClass the {@link WritableProperty#getValueClass() value class}.
   * @return the newly created and added property.
   * @throws IllegalStateException if this {@link WritableBean} already has such property, is {@link #isReadOnly()
   *         read-only}, or not {@link #isDynamic() dynamic}.
   */
  default <V> WritableProperty<V> createProperty(String name, Class<V> valueClass) {

    return createProperty(name, valueClass, valueClass);
  }

  /**
   * Creates and {@link #addProperty(WritableProperty) adds} a {@link WritableProperty} with the given
   * {@link WritableProperty#getValueClass() value class} dynamically.
   *
   * @param <V> the generic type of the {@link WritableProperty#getValueClass() value class}.
   * @param name the {@link WritableProperty#getName() property name}.
   * @param valueClass the {@link WritableProperty#getValueClass() value class}.
   * @param valueType the {@link Type} reflecting the {@link WritableProperty#get() property value}.
   * @return the newly created and added property.
   * @throws IllegalStateException if this {@link WritableBean} already has such property, is {@link #isReadOnly()
   *         read-only}, or not {@link #isDynamic() dynamic}.
   */
  <V> WritableProperty<V> createProperty(String name, Class<V> valueClass, Type valueType);

  /**
   * {@link #getProperty(String) Gets} or {@link #createProperty(String, Class) creates} the specified property.
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
  default <V> WritableProperty<V> getOrCreateProperty(String name, Class<V> valueClass) {

    return getOrCreateProperty(name, valueClass, valueClass);
  }

  /**
   * {@link #getProperty(String) Gets} or {@link #createProperty(String, Class, Type) creates} the specified property.
   *
   * @param <V> the generic type of the {@link WritableProperty#get() property value}.
   * @param name the {@link WritableProperty#getName() property name}.
   * @param valueClass the {@link Class} reflecting the {@link WritableProperty#get() property value}.
   * @param valueType the {@link Type} reflecting the {@link WritableProperty#get() property value}.
   * @return the requested property. Will be created if it does not already {@link #getProperty(String) exist}.
   * @throws IllegalArgumentException if the requested property already exists but has an incompatible
   *         {@link WritableProperty#getValueClass() value class}.
   * @throws IllegalStateException if the requested property does not exist but this {@link WritableBean} is
   *         {@link #isReadOnly() read-only}, or not {@link #isDynamic() dynamic}.
   */
  @SuppressWarnings("unchecked")
  default <V> WritableProperty<V> getOrCreateProperty(String name, Class<V> valueClass, Type valueType) {

    WritableProperty<?> property = getProperty(name);
    if (property != null) {
      if (valueClass.isAssignableFrom(property.getValueClass())) {
        return ((WritableProperty<V>) property);
      } else {
        throw new IllegalArgumentException("Property '" + name + "' with value class "
            + property.getValueClass().getName() + " was requested for mismatching value class " + valueClass);
      }
    }
    return createProperty(name, valueClass, valueType);
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  default void read(StructuredReader reader) {

    if (!reader.readStartObject() || reader.readEnd()) {
      return;
    }
    PropertyIdMapping idMapping = null;
    if (reader.getFormat().isIdBased()) {
      idMapping = PropertyIdMapper.get().getIdMapping(this);
    }
    while (!reader.readEnd()) {
      String propertyName = null;
      if (idMapping == null) {
        propertyName = reader.readName();
      } else {
        int propertyId = reader.readId();
        propertyName = idMapping.name(propertyId);
        if (propertyName == null) {
          propertyName = Integer.toString(propertyId);
        }
      }
      if (PROPERTY_TYPE_NAME.equals(propertyName)) {
        String type = reader.readValueAsString();
        String stableName = getType().getStableName();
        if (!type.equals(stableName)) {
          throw new IllegalStateException(PROPERTY_TYPE_NAME + "=" + type + "!=" + stableName);
        }
      } else {
        WritableProperty<?> property = getProperty(propertyName);
        if (property == null) {
          if (isDynamic()) {
            Object value = reader.readValue(true);
            if (value == null) {
              return; // ignore...
            }
            Class<? extends Object> valueClass = value.getClass();
            property = createProperty(propertyName, valueClass);
            ((WritableProperty) property).set(value);
          } else {
            // LOG.debug("ignoring undefined property {}.{}", getBeanClass(), propertyName);
            reader.skipValue();
          }
        } else {
          property.readObject(reader);
        }
      }
    }
  }

}
