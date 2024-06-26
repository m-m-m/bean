/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.util.Collection;

import io.github.mmm.bean.mapping.PropertyIdCollector;
import io.github.mmm.marshall.MarshallableObject;
import io.github.mmm.property.AttributeReadOnly;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.validation.Validatable;
import io.github.mmm.validation.ValidationResult;
import io.github.mmm.validation.ValidationResultBuilder;
import io.github.mmm.value.ReadablePath;

/**
 * Read interface of a {@link Bean} holding arbitrary {@link #getProperty(String) properties}. Unlike plain old Java
 * Beans this offers a lot of advanced features:
 * <ul>
 * <li><b>Simple</b> - no need to write boiler-plate code for implementation such as getters, setters, equals, or
 * hashCode.</li>
 * <li><b>Generic</b> - fast, easy and reliable introspection via {@link #getProperties() iteration of all properties}.
 * No more greedy and slow reflection at runtime (after bootstrapping or AOT compilation).</li>
 * <li><b>Dynamic</b> - supports combination of Java's strong typing with {@link #isDynamic() dynamic} beans. E.g. if
 * read data from Database, XML, or JSON you can still map "undefined" properties in your {@link Bean}. This way a
 * client can receive an object from a newer version of a database or service with added properties that will be kept in
 * the object and send back when the {@link Bean} is written back.</li>
 * <li><b>ReadOnly-Support</b> - create a {@link #isReadOnly() read-only} {@link WritableBean#getReadOnly() view} of
 * your bean to pass by reference without side-effects.</li>
 * <li><b>Copy-Support</b> - create a {@link #copy() copy} of your bean before passing around to avoid side-effects or
 * to create a mutable copy from a {@link #isReadOnly() read-only} bean.</li>
 * <li><b>Powerful</b> - {@link WritableProperty} supports listeners and bindings as well as
 * {@link WritableProperty#getValueClass() generic type information}.</li>
 * <li><b>Validation</b> - build-in {@link #validate() validation support}.</li>
 * <li><b>Marshalling</b> - build-in support to {@link WritableBean#read(io.github.mmm.marshall.StructuredReader) read}
 * and {@link #write(io.github.mmm.marshall.StructuredWriter) write} the {@link Bean} from/to JSON, XML, or other
 * formats. Implement custom datatypes as {@link io.github.mmm.property.Property property} and you will not need
 * separate classes or configurations for mapping.</li>
 * <li><b>Portable</b> - everything relies only on established Java standard mechanisms. No customization of build
 * processes, IDEs, etc. needed. It just works with any build tool (maven, gradle, buildr, ant, etc.) and IDE (Eclipse,
 * IntelliJ, NetBeans, etc.) without plugins and therefore will also work in the future whatever may come.</li>
 * </ul>
 */
public interface ReadableBean extends Validatable, MarshallableObject, AttributeReadOnly, ReadablePath {

  /**
   * The optional suffix for a property method (when following JavaFx conventions what is not recommended by mmm-bean).
   */
  String SUFFIX_PROPERTY = "Property";

  /**
   * @param name the {@link WritableProperty#getName() name} of the requested property or a potential
   *        {@link #getAliases() alias} of the property.
   * @return the requested {@link WritableProperty} or {@code null} if no such property exists.
   * @see WritableBean#addProperty(WritableProperty)
   * @see WritableBean#getOrCreateProperty(String, Class)
   */
  ReadableProperty<?> getProperty(String name);

  /**
   * @return a {@link Collection} with all {@link ReadableProperty properties} of this bean.
   */
  Collection<? extends ReadableProperty<?>> getProperties();

  /**
   * @return the number of {@link #getProperty(String) properties} of this {@link ReadableBean}.
   */
  int getPropertyCount();

  /**
   * @param name the {@link ReadableProperty#getName() name} of the requested property.
   * @return the requested {@link ReadableProperty property}.
   * @throws RuntimeException if the requested property does not exist.
   */
  default ReadableProperty<?> getRequiredProperty(String name) {

    ReadableProperty<?> property = getProperty(name);
    if (property == null) {
      throw new IllegalArgumentException(name);
    }
    return property;
  }

  /**
   * @param <V> type of the {@link ReadableProperty#get() property value}.
   * @param name the {@link ReadableProperty#getName() property name}.
   * @return the {@link ReadableProperty#get() value} of the {@link #getProperty(String) property with the given name}.
   *         Will be {@code null} if no such property exists or the {@link ReadableProperty#get() property value} is
   *         {@code null}.
   */
  @SuppressWarnings("unchecked")
  default <V> V get(String name) {

    ReadableProperty<?> property = getProperty(name);
    if (property == null) {
      return null;
    }
    return (V) property.get();
  }

  /**
   * @return the {@link BeanAliasMap} with potential aliases for {@link #getProperty(String) property}
   *         {@link ReadableProperty#getName() name}s.
   */
  BeanAliasMap getAliases();

  /**
   * @return the {@link BeanType} reflecting this {@link Bean}.
   * @see VirtualBean#getType()
   * @see BeanClass
   */
  BeanType getType();

  /**
   * @return the {@link Class} reflecting this bean. Please note that {@link VirtualBean}s may implement multiple
   *         interfaces as a virtual type that does not physically exist as Java {@link Class} and in such case the
   *         method will return the primary of these {@link Class}es (interfaces).
   */
  default Class<?> getJavaClass() {

    return getType().getJavaClass();
  }

  /**
   * @return {@code true} if this {@link Bean} is dynamic meaning that is not strictly typed but allows to dynamically
   *         add properties, {@code false} otherwise.
   * @see VirtualBean
   */
  boolean isDynamic();

  /**
   * @return {@code true} if this {@link Bean} is a {@link BeanClass}, {@code false} otherwise (it is a regular
   *         instance).
   * @see BeanClass#getPrototype()
   */
  boolean isPrototype();

  /**
   * A {@link Bean} may be polymorphic to allow mappings to and from other representations without knowing the exact
   * type. So assuming a service accepts or returns a {@link Bean} of a specific type that has sub-types. If that type
   * is declared as polymorphic then it is possible to unarshall the {@link Bean} of the exact sub-type back from its
   * serialized data.<br>
   * By default a {@link Bean} is not polymorphic. Once you declare your custom {@link Bean} as polymorphic by
   * overriding this method returning {@code true}, you may not this method it again. Hence, if you override this method
   * in a class, you should declare it as final.
   *
   * @return {@code true} if this {@link Bean} is polymorphic meaning it represents an entire hierarchy of {@link Bean}
   *         types, {@code false} otherwise.
   */
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

  /**
   * A {@link Bean} implementation shall not override {@link Object#equals(Object)} and {@link Object#hashCode()} for
   * efficient usage in {@link java.util.Collection}s and {@link java.util.Map}s. Hence the regular
   * {@link Object#equals(Object) equals} method will just check for object identity. For a logical equals check you may
   * use this method. Be aware that is may be expensive as it recursively traverses into all properties that may again
   * contain a {@link ReadableBean}.
   *
   * @param other the {@link ReadableBean} to compare with.
   * @return {@code true} if this {@link ReadableBean} is logically equal to the given {@link ReadableBean}, that is it
   *         has the same type and all {@link #getProperty(String) properties} are {@link Object#equals(Object) equal},
   *         {@code false} otherwise.
   */
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

  /**
   * @return a copy of this {@link WritableBean} that has the same values for all {@link #getProperties() properties}.
   */
  WritableBean copy();

  /**
   * @return a new instance of this {@link WritableBean}.
   */
  WritableBean newInstance();

  /**
   * Defines how the {@link #getProperties() properties} of this {@link ReadableBean} are mapped to numeric IDs. This is
   * only relevant in case you want to use binary protocols ProtoBuf/GRPC.
   *
   * @param mapping the {@link PropertyIdCollector} used to receive the mapping.
   */
  default void mapPropertyIds(PropertyIdCollector mapping) {

  }

  /**
   * Method to implement {@link #equals(Object)} directly in a bean interface as default method. For simplification it
   * is only called if the object to compare to is not {@code null} and has the same {@link #getType() type} so you do
   * not have to handle these cases in your custom implementation.<br>
   * <b>ATTENTION:</b> It is rather discouraged to override this method. Simply use {@link #isEqual(ReadableBean)}
   * instead of {@link #equals(Object)} when you want comparison by value (e.g. to check for duplicates).
   *
   * @param other the object to compare to. Will not be {@code null} and has the same {@link #getType() type} as this
   *        bean.
   * @return {@code true} if this and the given {@link ReadableBean} are considered equals, {@code false} otherwise.
   * @see #equals(Object)
   * @see #isEqual(ReadableBean)
   */
  default boolean doEquals(ReadableBean other) {

    return (this == other);
  }

  /**
   * Method to implement {@link #toString()} directly in a bean interface as default method.
   *
   * @return the {@link Object#toString() string representation of this bean}.
   */
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

  /**
   * @param sb the {@link StringBuilder} where to append the details (the properties) of this {@link Bean} for
   *        {@link #toString()}-Representation.
   */
  default void toString(StringBuilder sb) {

    for (ReadableProperty<?> property : getProperties()) {
      property.toString(sb);
      sb.append(",");
    }
  }

  /**
   * @param <B> type of the {@link WritableBean}.
   * @param bean the {@link WritableBean} to create a {@link #newInstance() new instance} of.
   * @return the {@link #newInstance() new instance}.
   */
  @SuppressWarnings("unchecked")
  static <B extends ReadableBean> B newInstance(B bean) {

    if (bean == null) {
      return null;
    }
    return (B) bean.newInstance();
  }

  /**
   * @param <B> type of the {@link ReadableBean}.
   * @param bean the {@link ReadableBean bean} to {@link #copy() copy}.
   * @return the {@link #copy() copy}.
   */
  @SuppressWarnings("unchecked")
  static <B extends ReadableBean> B copy(B bean) {

    if (bean == null) {
      return null;
    }
    return (B) bean.copy();
  }

  /**
   * @param <B> type of the {@link ReadableBean}.
   * @param bean the {@link ReadableBean bean} to get the Java {@link Class} for.
   * @return the {@link Class} reflecting the given {@link ReadableBean bean}.
   */
  @SuppressWarnings("unchecked")
  static <B extends ReadableBean> Class<B> getJavaClass(B bean) {

    return (Class<B>) bean.getJavaClass();
  }

}
