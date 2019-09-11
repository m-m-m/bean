/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

import java.lang.reflect.Type;

import net.sf.mmm.marshall.StructuredReader;
import net.sf.mmm.marshall.UnmarshallableObject;
import net.sf.mmm.property.WritableProperty;

/**
 * Writable interface of {@link ReadableBean}.
 */
public interface WritableBean extends ReadableBean, UnmarshallableObject {

  @Override
  WritableProperty<?> getProperty(String name);

  @Override
  Iterable<? extends WritableProperty<?>> getProperties();

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
   * @param value new {@link WritableProperty#getValue() value} of the specified property.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  default void set(String name, Object value) {

    WritableProperty property = getRequiredProperty(name);
    property.setValue(value);
  }

  /**
   * /** Sets the value of the {@link #getOrCreateProperty(String, Class) existing or newly created property} with the
   * given {@code name} to the given value.
   *
   * @param <V> the generic type of the {@link WritableProperty#getValueClass() value class}.
   * @param name the {@link WritableProperty#getName() property name}.
   * @param value new {@link WritableProperty#getValue() value} of the specified property.
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
   * @param value new {@link WritableProperty#getValue() value} of the specified property.
   * @param valueClass the {@link WritableProperty#getValueClass() value class}.
   * @param valueType the {@link Type} reflecting the {@link WritableProperty#getValue() property value}.
   * @see #set(String, Object)
   */
  default <V> void set(String name, V value, Class<V> valueClass, Type valueType) {

    WritableProperty<V> property = getOrCreateProperty(name, valueClass, valueType);
    property.setValue(value);
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
   * @param valueType the {@link Type} reflecting the {@link WritableProperty#getValue() property value}.
   * @return the newly created and added property.
   * @throws IllegalStateException if this {@link WritableBean} already has such property, is {@link #isReadOnly()
   *         read-only}, or not {@link #isDynamic() dynamic}.
   */
  <V> WritableProperty<V> createProperty(String name, Class<V> valueClass, Type valueType);

  /**
   * {@link #getProperty(String) Gets} or {@link #createProperty(String, Class) creates} the specified property.
   *
   * @param <V> the generic type of the {@link WritableProperty#getValue() property value}.
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
   * @param <V> the generic type of the {@link WritableProperty#getValue() property value}.
   * @param name the {@link WritableProperty#getName() property name}.
   * @param valueClass the {@link Class} reflecting the {@link WritableProperty#getValue() property value}.
   * @param valueType the {@link Type} reflecting the {@link WritableProperty#getValue() property value}.
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

    while (!reader.readEnd()) {
      String propertyName = reader.readName();
      if (PROPERTY_TYPE.equals(propertyName)) {
        String type = reader.readValueAsString();
        String stableName = getType().getStableName();
        if (!type.equals(stableName)) {
          throw new IllegalStateException(PROPERTY_TYPE + "=" + type + "!=" + stableName);
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
            ((WritableProperty) property).setValue(value);
          } else {
            // LOG.debug("ignoring undefined property {}.{}", getBeanClass(), propertyName);
            reader.skipValue();
          }
        } else {
          property.read(reader);
        }
      }
    }
  }

  /**
   * @param <B> type of the {@link WritableBean}.
   * @param bean the {@link WritableBean} to get a {@link #getReadOnly() read-only view} for.
   * @return the {@link #getReadOnly() read-only view}.
   */
  @SuppressWarnings("unchecked")
  static <B extends WritableBean> B getReadOnly(B bean) {

    if (bean == null) {
      return null;
    } else if (bean.isReadOnly()) {
      return bean;
    } else {
      return (B) bean.getReadOnly();
    }
  }

  /**
   * @return a {@link #isReadOnly() immutable} view of this {@link WritableBean}. Read operations on this
   *         {@link #isReadOnly() immutable} view will produce the exact same results as on the original
   *         {@link WritableBean}. However, all write operations will fail.
   * @see #getReadOnly(Bean)
   */
  WritableBean getReadOnly();

}
