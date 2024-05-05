/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.examples;

import io.github.mmm.bean.Bean;
import io.github.mmm.bean.BeanName;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.number.integers.IntegerProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * A {@link Bean} for testing.
 */
@BeanName("mmm_TestBean")
public class TestBean extends Bean {

  /** Full name of person. */
  public final StringProperty Name;

  /** Age of person. */
  public final IntegerProperty Age;

  /**
   * The constructor.
   */
  public TestBean() {

    this(null);
  }

  /**
   * The constructor.
   *
   * @param writable the {@link WritableBean} to wrap as {@link #isReadOnly() read-only} bean or {@code null} to create
   *        a mutable bean.
   */
  public TestBean(WritableBean writable) {

    super(writable);
    this.Name = add().newString("Name");
    this.Age = add().newInteger("Age");
  }

}
