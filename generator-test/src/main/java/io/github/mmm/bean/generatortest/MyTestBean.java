/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.generatortest;

import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.VirtualBean;
import io.github.mmm.property.number.integers.IntegerProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * Test bean.
 */
@SuppressWarnings("javadoc")
public interface MyTestBean extends VirtualBean {

  @Mandatory
  StringProperty Name();

  IntegerProperty Age();

}
