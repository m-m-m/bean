/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.util.HashSet;
import java.util.Set;

import io.github.mmm.property.ReadableProperty;

/// Helper class for [io.github.mmm.bean.Bean]s.
class BeanTestHelper {

  /// @param bean the [ReadableBean].
  /// @return the [Set] with the names of the [properties][ReadableBean#getProperties()].
  public static Set<String> getPropertyNames(ReadableBean bean) {

    Set<String> names = new HashSet<>();
    for (ReadableProperty<?> property : bean.getProperties()) {
      names.add(property.getName());
    }
    return names;
  }

}
