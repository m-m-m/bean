/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

import net.sf.mmm.property.number.integers.IntegerProperty;

/**
 * {@link AdvancedBean} for testing.
 */
@SuppressWarnings("javadoc")
@Name("mmm.TestAdvancedBean")
public class TestAdvancedBean extends TestAdvancedParentBean {

  /** @see BeanClass#getPrototype() */
  @SuppressWarnings("hiding")
  public static final TestAdvancedBean PROTOTYPE = new TestAdvancedBean();

  /** Age of person. */
  public final IntegerProperty Age;

  public TestAdvancedBean() {

    this(null, true, null);
  }

  public TestAdvancedBean(boolean dynamic) {

    this(null, dynamic, null);
  }

  public TestAdvancedBean(AbstractBean writable, boolean dynamic) {

    this(writable, dynamic, null);
  }

  public TestAdvancedBean(boolean dynamic, BeanClass type) {

    this(null, dynamic, type);
  }

  public TestAdvancedBean(AbstractBean writable, boolean dynamic, BeanClass type) {

    super(writable, dynamic, type);
    this.Age = add(new IntegerProperty("Age"));
  }

  @Override
  protected AbstractBean create(AbstractBean writableBean, boolean dynamicFlag) {

    return new TestAdvancedBean(writableBean, dynamicFlag, getType());
  }

}
