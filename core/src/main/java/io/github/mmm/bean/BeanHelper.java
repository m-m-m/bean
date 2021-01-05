/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.util.HashSet;
import java.util.Set;

import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;

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

    // TODO: also copy dynamic properties?
    for (WritableProperty property : copy.getProperties()) {
      Object value = source.get(property.getName());
      property.set(value);
    }
    if (readOnly) {
      copy.makeReadOnly();
    }
  }

  /**
   * @param getter the name of a method that is potentially a getter.
   * @return the property name for the given {@code getter} or {@code null} if not a getter.
   */
  public static String getPropertyForGetter(String getter) {

    return getCapitalSuffixAfterPrefixes(getter, "get", "has", "is");
  }

  /**
   * @param setter the name of a method that is potentially a setter.
   * @return the property name for the given {@code setter} or {@code null} if not a setter.
   */
  public static String getPropertyForSetter(String setter) {

    return getCapitalSuffixAfterPrefix(setter, "set");
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
}
