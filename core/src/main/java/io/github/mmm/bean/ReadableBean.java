/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.util.Collection;

import io.github.mmm.bean.mapping.PropertyIdCollector;
import io.github.mmm.marshall.MarshallableObject;
import io.github.mmm.property.PropertyLock;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.validation.Validatable;
import io.github.mmm.validation.ValidationResult;
import io.github.mmm.validation.ValidationResultBuilder;
import io.github.mmm.value.ReadablePath;

/// Read interface of a [Bean] holding arbitrary [properties][#getProperty(String)]. Unlike plain old Java
/// Beans this offers a lot of advanced features:
///
/// - **Simple** - no need to write boiler-plate code for implementation such as getters, setters, equals, or
/// hashCode.
/// - **Generic** - fast, easy and reliable introspection via [iteration of all properties][#getProperties()].
/// No more greedy and slow reflection at runtime (after bootstrapping or AOT compilation).
/// - **Dynamic** - supports combination of Java's strong typing with [dynamic][#isDynamic()] beans. E.g. if
/// read data from Database, XML, or JSON you can still map "undefined" properties in your [Bean]. This way a
/// client can receive an object from a newer version of a database or service with added properties that will be kept in
/// the object and send back when the [Bean] is written back.
/// - **ReadOnly-Support** - create a [read-only][#isReadOnly()] [view][WritableBean#getReadOnly()] of
/// your bean to pass by reference without side-effects.
/// - **Copy-Support** - create a [copy][#copy()] of your bean before passing around to avoid side-effects or
/// to create a mutable copy from a [read-only][#isReadOnly()] bean.
/// - **Powerful** - [WritableProperty] supports listeners and bindings as well as
/// [generic type information][WritableProperty#getValueClass()].
/// - **Validation** - build-in [validation support][#validate()].
/// - **Marshalling** - build-in support to [read][WritableBean#read(io.github.mmm.marshall.StructuredReader)]
/// and [write][#write(io.github.mmm.marshall.StructuredWriter)] the [Bean] from/to JSON, XML, or other
/// formats. Implement custom datatypes as [property][io.github.mmm.property.Property] and you will not need
/// separate classes or configurations for mapping.
/// - **Portable** - everything relies only on established Java standard mechanisms. No customization of build
/// processes, IDEs, etc. needed. It just works with any build tool (maven, gradle, buildr, ant, etc.) and IDE (Eclipse,
/// IntelliJ, NetBeans, etc.) without plugins and therefore will also work in the future whatever may come.
public interface ReadableBean extends Validatable, MarshallableObject, BeanLock, ReadablePath {

  /// The optional suffix for a property method (when following JavaFx conventions what is not recommended by mmm-bean).
  String SUFFIX_PROPERTY = "Property";

  /// @param name the [name][WritableProperty#getName()] of the requested property or a potential
  ///        [alias][#getAliases()] of the property.
  /// @return the requested [WritableProperty] or `null` if no such property exists.
  /// @see WritableBean#addProperty(WritableProperty)
  /// @see WritableBean#getOrCreateProperty(String, Class)
  ReadableProperty<?> getProperty(String name);

  /// @return a [Collection] with all [properties][ReadableProperty] of this bean.
  Collection<? extends ReadableProperty<?>> getProperties();

  /// @return the number of [properties][#getProperty(String)] of this [ReadableBean].
  int getPropertyCount();

  /// @param name the [name][ReadableProperty#getName()] of the requested property.
  /// @return the requested [property][ReadableProperty].
  /// @throws RuntimeException if the requested property does not exist.
  default ReadableProperty<?> getRequiredProperty(String name) {

    ReadableProperty<?> property = getProperty(name);
    if (property == null) {
      throw new IllegalArgumentException(name);
    }
    return property;
  }

  /// @param <V> type of the [property value][ReadableProperty#get()].
  /// @param name the [property name][ReadableProperty#getName()].
  /// @return the [value][ReadableProperty#get()] of the [property with the given name][#getProperty(String)].
  ///         Will be `null` if no such property exists or the [property value][ReadableProperty#get()] is
  ///         `null`.
  @SuppressWarnings("unchecked")
  default <V> V get(String name) {

    ReadableProperty<?> property = getProperty(name);
    if (property == null) {
      return null;
    }
    return (V) property.get();
  }

  /// @return the [BeanAliasMap] with potential aliases for [property][#getProperty(String)]
  ///         [name][ReadableProperty#getName()]s.
  BeanAliasMap getAliases();

  /// @return the [Class] reflecting this bean. Please note that [VirtualBean]s may implement multiple
  ///         interfaces as a virtual type that does not physically exist as Java [Class] and in such case the
  ///         method will return the primary of these [Class]es (interfaces).
  /// @see #getJavaClass(ReadableBean)
  default Class<?> getJavaClass() {

    return getType().getJavaClass();
  }

  /// @return `true` if this [Bean] is dynamic meaning that is not strictly typed but allows to dynamically
  ///         add properties, `false` otherwise.
  /// @see VirtualBean
  boolean isDynamic();

  /// @return `true` if this [Bean] is a [BeanClass], `false` otherwise (it is a regular
  ///         instance).
  /// @see BeanClass#getPrototype()
  boolean isPrototype();

  /// A [Bean] may be polymorphic to allow mappings to and from other representations without knowing the exact
  /// type. So assuming a service accepts or returns a [Bean] of a specific type that has sub-types. If that type
  /// is declared as polymorphic then it is possible to unmarshall the [Bean] of the exact sub-type back from its
  /// serialised data.
  /// By default a [Bean] is not polymorphic. Once you declare your custom [Bean] as polymorphic by
  /// overriding this method returning `true`, you may not this method it again. Hence, if you override this method
  /// in a class, you should declare it as final.
  ///
  /// @return `true` if this [Bean] is polymorphic meaning it represents an entire hierarchy of [Bean]
  ///         types, `false` otherwise.
  default boolean isPolymorphic() {

    return false;
  }

  @Override
  default ValidationResult validate() {

    ValidationResultBuilder builder = new ValidationResultBuilder();
    for (ReadableProperty<?> property : getProperties()) {
      ValidationResult result = property.validate();
      builder.add(result);
    }
    return builder.build(getType().getStableName());
  }

  /// A [Bean] implementation shall not override [Object#equals(Object)] and [Object#hashCode()] for
  /// efficient usage in [java.util.Collection]s and [java.util.Map]s. Hence the regular
  /// [equals][Object#equals(Object)] method will just check for object identity. For a logical equals check you may
  /// use this method. Be aware that is may be expensive as it recursively traverses into all properties that may again
  /// contain a [ReadableBean].
  ///
  /// @param other the [ReadableBean] to compare with.
  /// @return `true` if this [ReadableBean] is logically equal to the given [ReadableBean], that is it
  ///         has the same type and all [properties][#getProperty(String)] are [equal][Object#equals(Object)],
  ///         `false` otherwise.
  default boolean isEqual(ReadableBean other) {

    if (other == null) {
      return false;
    } else if (getPropertyCount() != other.getPropertyCount()) {
      return false;
    } else if (getType() != other.getType()) {
      return false;
    }
    for (ReadableProperty<?> property : getProperties()) {
      String name = property.getName();
      ReadableProperty<?> otherProperty = other.getProperty(name);
      if (!property.isEqual(otherProperty)) {
        return false;
      }
    }
    return true;
  }

  /// @return a copy of this [WritableBean] that has the same values for all [properties][#getProperties()].
  WritableBean copy();

  /// @return a new instance of this [WritableBean].
  WritableBean newInstance();

  /// Defines how the [properties][#getProperties()] of this [ReadableBean] are mapped to numeric IDs. This is
  /// only relevant in case you want to use binary protocols ProtoBuf/GRPC.
  ///
  /// @param mapping the [PropertyIdCollector] used to receive the mapping.
  default void mapPropertyIds(PropertyIdCollector mapping) {

  }

  /// Method to implement [#equals(Object)] directly in a bean interface as default method. For simplification it
  /// is only called if the object to compare to is not `null` and has the same [type][#getType()] so you do
  /// not have to handle these cases in your custom implementation.
  /// **ATTENTION:** It is rather discouraged to override this method. Simply use [#isEqual(ReadableBean)]
  /// instead of [#equals(Object)] when you want comparison by value (e.g. to check for duplicates).
  ///
  /// @param other the object to compare to. Will not be `null` and has the same [type][#getType()] as this
  ///        bean.
  /// @return `true` if this and the given [ReadableBean] are considered equals, `false` otherwise.
  /// @see #equals(Object)
  /// @see #isEqual(ReadableBean)
  default boolean doEquals(ReadableBean other) {

    return (this == other);
  }

  /// Method to implement [#toString()] directly in a bean interface as default method.
  ///
  /// @return the [string representation of this bean][Object#toString()].
  default String doToString() {

    StringBuilder sb = new StringBuilder(getType().getStableName());
    sb.append("(");
    sb.append("readonly=");
    sb.append(isReadOnly());
    sb.append(',');
    if (isDynamic()) {
      sb.append("dynamic,");
    }
    toString(sb);
    sb.setLength(sb.length() - 1);
    sb.append(")");
    return sb.toString();
  }

  /// @param sb the [StringBuilder] where to append the details (the properties) of this [Bean] for
  ///        [#toString()]-Representation.
  default void toString(StringBuilder sb) {

    for (ReadableProperty<?> property : getProperties()) {
      property.toString(sb);
      sb.append(",");
    }
  }

  /// @param <B> type of the [WritableBean].
  /// @param bean the [WritableBean] to create a [new instance][#newInstance()] of.
  /// @return the [new instance][#newInstance()].
  @SuppressWarnings("unchecked")
  static <B extends ReadableBean> B newInstance(B bean) {

    if (bean == null) {
      return null;
    }
    return (B) bean.newInstance();
  }

  /// @param <B> type of the [ReadableBean].
  /// @param bean the [bean][ReadableBean] to [copy][#copy()].
  /// @return the [copy][#copy()].
  @SuppressWarnings("unchecked")
  static <B extends ReadableBean> B copy(B bean) {

    if (bean == null) {
      return null;
    }
    return (B) bean.copy();
  }

  /// @param <B> type of the [ReadableBean].
  /// @param bean the [bean][ReadableBean] to get the Java [Class] for.
  /// @return the [Class] reflecting the given [bean][ReadableBean].
  @SuppressWarnings("unchecked")
  static <B extends ReadableBean> Class<B> getJavaClass(B bean) {

    return (Class<B>) bean.getJavaClass();
  }

  /// @param <B> type of the [ReadableBean] owning the [property][ReadableProperty].
  /// @param property the [ReadableProperty] for which the owning [ReadableBean] is requested.
  /// @return the [ReadableBean] owning the given [property][ReadableProperty]. Will be `null` if the
  ///         [ReadableProperty] is manually created from outside a bean.
  @SuppressWarnings("unchecked")
  static <B extends ReadableBean> B from(ReadableProperty<?> property) {

    PropertyLock lock = property.getMetadata().getLock();
    if (lock instanceof ReadableBean) {
      return (B) lock;
    }
    return null;
  }

}
