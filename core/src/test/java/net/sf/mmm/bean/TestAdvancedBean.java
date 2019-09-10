/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

import net.sf.mmm.property.number.integers.IntegerProperty;
import net.sf.mmm.property.string.StringProperty;

/**
 * {@link AdvancedBean} for testing.
 */
@SuppressWarnings("javadoc")
public class TestAdvancedBean extends AdvancedBean {

  /** Full name of person. */
  public final StringProperty Name;

  /** Age of person. */
  public final IntegerProperty Age;

  public TestAdvancedBean() {

    this(null, false, null);
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
    this.Name = add(new StringProperty("Name"));
    this.Age = add(new IntegerProperty("Age"));
  }

  @Override
  protected AbstractBean create(AbstractBean writableBean, boolean dynamicFlag) {

    return new TestAdvancedBean(writableBean, dynamicFlag, getType());
  }

}
