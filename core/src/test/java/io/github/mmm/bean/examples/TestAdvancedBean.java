/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.examples;

import io.github.mmm.bean.AdvancedBean;
import io.github.mmm.bean.BeanClass;
import io.github.mmm.bean.BeanName;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.number.integers.IntegerProperty;

/**
 * {@link AdvancedBean} for testing.
 */
@SuppressWarnings("javadoc")
@BeanName("mmm_TestAdvancedBean")
public class TestAdvancedBean extends TestAdvancedParentBean {

  /** @see BeanClass#getPrototype() */
  @SuppressWarnings("hiding")
  public static final TestAdvancedBean PROTOTYPE = new TestAdvancedBean();

  /** Age of person. */
  public final IntegerProperty Age;

  public TestAdvancedBean() {

    this(null, null);
  }

  public TestAdvancedBean(WritableBean writable) {

    this(writable, null);
  }

  public TestAdvancedBean(BeanClass type) {

    this(null, type);
  }

  public TestAdvancedBean(WritableBean writable, BeanClass type) {

    super(writable, type);
    this.Age = add().newInteger("Age");
  }

}
