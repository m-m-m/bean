/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.examples;

import io.github.mmm.bean.Bean;
import io.github.mmm.bean.Name;
import io.github.mmm.property.number.integers.IntegerProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * A {@link Bean} for testing.
 */
@Name("mmm_TestBean")
public class TestBean extends Bean {

  /** Full name of person. */
  public final StringProperty Name;

  /** Age of person. */
  public final IntegerProperty Age;

  /**
   * The constructor.
   */
  public TestBean() {

    super();
    this.Name = add().newString("Name");
    this.Age = add().newInteger("Age");
  }

}
