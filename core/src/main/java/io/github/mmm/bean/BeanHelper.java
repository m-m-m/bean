/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Set;

import io.github.mmm.bean.mapping.PropertyIdMapping;
import io.github.mmm.marshall.size.StructuredFormatSizeComputor;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.factory.PropertyFactory;
import io.github.mmm.property.factory.PropertyFactoryManager;
import io.github.mmm.property.factory.SimplePropertyFactory;

/**
 * Class with helper methods for internal reuse to avoid redundancies.
 *
 * @since 1.0.0
 */
public class BeanHelper {

  /**
   * @param source the source bean to copy from.
   * @param copy the target bean to copy to.
   * @param readOnly - {@code true} if the copy shall be read-only, {@code false} otherwise.
   * @see ReadableBean#copy(boolean)
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static void copy(AbstractBean source, AbstractBean copy, boolean readOnly) {

    for (WritableProperty property : copy.getProperties()) {
      if (!property.isReadOnly()) {
        Object value = source.get(property.getName());
        property.set(value);
      }
    }
    if (readOnly) {
      copy.makeReadOnly();
    }
  }

  /**
   * @param getter the name of a method that is potentially a getter.
   * @return the property name for the given {@code getter} or {@code null} if not a getter.
   */
  public static String getPropertyName4Getter(String getter) {

    return getCapitalSuffixAfterPrefixes(getter, "get", "has", "is");
  }

  /**
   * @param setter the name of a method that is potentially a setter.
   * @return the property name for the given {@code setter} or {@code null} if not a setter.
   */
  public static String getPropertyName4Setter(String setter) {

    return getCapitalSuffixAfterPrefix(setter, "set");
  }

  /**
   * @param method the {@link Method} to check.
   * @return the property name for the given {@link Method} or {@code null} if not a property {@link Method}.
   */
  public static String getPropertyName4Property(Method method) {

    if (method.getParameterCount() == 0) {
      String methodName = method.getName();
      char first = methodName.charAt(0);
      if (Character.isUpperCase(first)) {
        return methodName;
      } else if (methodName.endsWith(ReadableBean.SUFFIX_PROPERTY)) {
        return Character.toUpperCase(first)
            + methodName.substring(1, methodName.length() - ReadableBean.SUFFIX_PROPERTY.length());
      }
    }
    return null;
  }

  /**
   * @param type the {@link Class} reflecting the {@link ReadableProperty} to get the {@link PropertyFactory} for.
   * @return the {@link PropertyFactory} for the given property {@code Class} or {@code null} if not a property.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static PropertyFactory<?, ?> getPropertyFactory(Class<?> type) {

    if (ReadableProperty.class.isAssignableFrom(type)) {
      Class propertyType = type;
      PropertyFactory<?, ?> factory = PropertyFactoryManager.get().getFactoryForPropertyType(propertyType);
      if (factory == null) {
        if (!type.isInterface() && !Modifier.isAbstract(type.getModifiers())) {
          return new SimplePropertyFactory<>(propertyType);
        }
        throw new IllegalStateException("Missing PropertyFactory for type: " + type.getName());
      }
      return factory;
    }
    return null;
  }

  /**
   * @param string the {@link String} to convert (e.g. method name).
   * @param prefixes the array of prefixes to remove.
   * @return the first {@link #getCapitalSuffixAfterPrefix(String, String) capital suffix} or {@code null} of no
   *         {@code prefix} was matched.
   */
  private static String getCapitalSuffixAfterPrefixes(String string, String... prefixes) {

    for (String prefix : prefixes) {
      String suffix = getCapitalSuffixAfterPrefix(string, prefix);
      if (suffix != null) {
        return suffix;
      }
    }
    return null;
  }

  /**
   * @param string the {@link String} to convert (e.g. method name).
   * @param prefix the prefix to remove.
   * @return the capitalized rest after the given {@code prefix} or {@code null} if the {@code prefix} was not present.
   */
  private static String getCapitalSuffixAfterPrefix(String string, String prefix) {

    if (string.startsWith(prefix)) {
      String suffix = string.substring(prefix.length());
      if ((suffix.length() > 0) && (Character.isUpperCase(suffix.charAt(0)))) {
        return suffix;
      }
    }
    return null;
  }

  /**
   * @param bean the {@link ReadableBean}.
   * @return the {@link Set} with the names of the {@link ReadableBean#getProperties() properties}.
   */
  public static Set<String> getPropertyNames(ReadableBean bean) {

    Set<String> names = new HashSet<>();
    for (ReadableProperty<?> property : bean.getProperties()) {
      names.add(property.getName());
    }
    return names;
  }

  /**
   * ATTENTION: This is an internal method that should not be used from outside of this project.
   *
   * @param bean the {@link ReadableBean}.
   * @param computor the {@link StructuredFormatSizeComputor}.
   * @param idMapping the {@link PropertyIdMapping}.
   * @return the computed size.
   */
  public static int computeSize(ReadableBean bean, StructuredFormatSizeComputor computor, PropertyIdMapping idMapping) {

    return ((AbstractBean) bean).computeSize(computor, idMapping);
  }
}
