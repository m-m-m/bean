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

/// Writable interface of [ReadableBean].
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

  /// Sets the value of the [required property][#getRequiredProperty(String)] with the given `name` to the
  /// given `value`.
  ///
  /// @param name the [name][WritableProperty#getName()] of the property.
  /// @param value new [value][WritableProperty#get()] of the specified property.
  @SuppressWarnings({ "rawtypes", "unchecked" })
  default void set(String name, Object value) {

    WritableProperty property = getRequiredProperty(name);
    property.set(value);
  }

  /// Sets the value of the [property][#getProperty(String)] with the given `name` to the given `value`.
  /// If no such [property][#getProperty(String)] exists, it will be [created][#createProperty(String, Class)]
  /// [dynamically][#isDynamic()] if the given value is not `null`.
  ///
  /// @param name the [name][WritableProperty#getName()] of the property.
  /// @param value new [value][WritableProperty#get()] of the specified property.
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

  /// /** Sets the value of the [existing or newly created property][#getOrCreateProperty(String, Class)] with the
  /// given `name` to the given `value`.
  ///
  /// @param <V> the generic type of the [value class][WritableProperty#getValueClass()].
  /// @param name the [property name][WritableProperty#getName()].
  /// @param value new [value][WritableProperty#get()] of the specified property.
  /// @param valueClass the [value class][WritableProperty#getValueClass()].
  /// @see #set(String, Object)
  default <V> void set(String name, V value, Class<V> valueClass) {

    WritableProperty<V> property = getOrCreateProperty(name, valueClass);
    property.set(value);
  }

  /// Adds the given [WritableProperty] to this bean.
  ///
  /// @param <P> type of the [WritableProperty] to add.
  /// @param property the [WritableProperty] to add.
  /// @return the given `property`.
  /// @throws IllegalStateException if this [WritableBean] is [read-only][#isReadOnly()] or not
  ///         [dynamic][#isDynamic()].
  <P extends WritableProperty<?>> P addProperty(P property);

  /// Creates and [adds][#addProperty(WritableProperty)] a [WritableProperty] with the given
  /// [value class][WritableProperty#getValueClass()] [dynamically][#isDynamic()].
  ///
  /// @param <V> the generic type of the [value class][WritableProperty#getValueClass()].
  /// @param name the [property name][WritableProperty#getName()].
  /// @param valueClass the [value class][WritableProperty#getValueClass()].
  /// @return the newly created and added property.
  /// @throws IllegalStateException if this [WritableBean] already has such property, is [read-only][#isReadOnly()], or not [dynamic][#isDynamic()].
  <V> WritableProperty<V> createProperty(String name, Class<V> valueClass);

  /// [Gets][#getProperty(String)] or [creates][#createProperty(String, Class)] the specified property
  /// [dynamically][#isDynamic()].
  ///
  /// @param <V> the generic type of the [property value][WritableProperty#get()].
  /// @param name the [property name][WritableProperty#getName()].
  /// @param valueClass the [value class][WritableProperty#getValueClass()] of the requested property.
  /// @return the requested property. Will be created if it does not already [exist][#getProperty(String)].
  /// @throws IllegalArgumentException if the requested property already exists but has an incompatible
  ///         [value class][WritableProperty#getValueClass()].
  /// @throws IllegalStateException if the requested property does not exist but this [WritableBean] is
  ///         [read-only][#isReadOnly()], or not [dynamic][#isDynamic()].
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

  /// @return the [read only][#isReadOnly()] view on this [bean][WritableBean].
  /// @see #isReadOnly()
  /// @see #getReadOnly(WritableBean)
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
        } else {
          if (isPolymorphic()) {
            // do not use import statement here to prevent early class-loading...
            Class beanClass = io.github.mmm.bean.mapping.ClassNameMapper.get().getClass(type);
            result = BeanFactory.get().create(beanClass);
          } else if (!type.equals(stableName)) {
            throw new IllegalStateException(StructuredReader.TYPE + "=" + type + "!=" + stableName);
          }
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

  /// @return the optional alias of this bean. It will be appended as prefix to the [#path()] of all
  ///         [properties][#getProperties()]. This is useful for API to build queries. So e.g. set to "alias" and
  ///         your contained property will have the path "alias.MyProperty".
  @Override
  String pathSegment();

  /// Used to set an alias as described in [#pathSegment()].
  ///
  /// **ATTENTION:** End users should never use this method directly but use designated methods such as
  /// `as(String alias)` provided by according SQL clauses.
  @Override
  void pathSegment(String pathSegment);

  /// @param <B> type of the [WritableBean].
  /// @param bean the [WritableBean] to get a [read-only][#isReadOnly()] [view][#getReadOnly()] of.
  /// @return the [read-only][#isReadOnly()] [view][#getReadOnly()] of the given [WritableBean].
  @SuppressWarnings("unchecked")
  static <B extends WritableBean> B getReadOnly(B bean) {

    return (B) bean.getReadOnly();
  }

  /// @param <B> type of the [WritableBean] owning the [property][WritableProperty].
  /// @param property the [WritableProperty] for which the owning [WritableBean] is requested.
  /// @return the [WritableBean] owning the given [property][WritableProperty]. Will be `null` if the
  ///         [WritableProperty] is manually created from outside a bean.
  @SuppressWarnings("unchecked")
  static <B extends WritableBean> B from(WritableProperty<?> property) {

    AttributeReadOnly lock = property.getMetadata().getLock();
    if (lock instanceof WritableBean) {
      return (B) lock;
    }
    return null;
  }

  /// @param <B> type of the [WritableBean] to read.
  /// @param template an instance of the [WritableBean] to read acting as template.
  /// @param reader the [StructuredReader] to read the data from.
  /// @return the [List] with the the unmarshalled [beans][WritableBean].
  static <B extends WritableBean> List<B> readArray(B template, StructuredReader reader) {

    List<B> list = new ArrayList<>();
    readArray(template, reader, list);
    return list;
  }

  /// @param <B> type of the [WritableBean] to read.
  /// @param template an instance of the [WritableBean] to read acting as template.
  /// @param reader the [StructuredReader] to read the data from.
  /// @param collection the [Collection] where to [add][Collection#add(Object)] the unmarshalled
  ///        [beans][WritableBean].
  static <B extends WritableBean> void readArray(B template, StructuredReader reader, Collection<B> collection) {

    reader.require(StructuredState.START_ARRAY, true);
    while (!reader.readEndArray()) {
      B bean = ReadableBean.newInstance(template);
      bean.read(reader);
      collection.add(bean);
    }
  }

  /// @param <B> type of the [WritableBean] to write.
  /// @param beans the [Collection] with the [beans][WritableBean] to write.
  /// @param writer the [StructuredWriter] to write to.
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
