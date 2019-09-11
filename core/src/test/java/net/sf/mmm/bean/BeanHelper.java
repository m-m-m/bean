/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

import java.util.HashSet;
import java.util.Set;

import net.sf.mmm.property.ReadableProperty;

/**
 * Helper class for {@link Bean}s.
 */
public class BeanHelper {

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
