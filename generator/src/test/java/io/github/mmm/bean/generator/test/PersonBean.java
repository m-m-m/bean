/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.generator.test;

import io.github.mmm.bean.VirtualBean;
import io.github.mmm.property.number.integers.IntegerProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * {@link VirtualBean} to test dynamic creation from interface.
 */
@SuppressWarnings("javadoc")
public interface PersonBean extends VirtualBean {

  StringProperty Name();

  String getName();

  void setName(String name);

  IntegerProperty Age();

  Integer getAge();

}
