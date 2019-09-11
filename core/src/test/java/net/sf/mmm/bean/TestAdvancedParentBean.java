/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

import net.sf.mmm.property.string.StringProperty;

/**
 * {@link AdvancedBean} for testing.
 */
@SuppressWarnings("javadoc")
@Name("mmm.TestAdvancedParentBean")
public class TestAdvancedParentBean extends AdvancedBean {

  static {
    new TestAdvancedParentBean();
  }

  /** Full name of person. */
  public final StringProperty Name;

  public TestAdvancedParentBean() {

    this(null, false, null);
  }

  public TestAdvancedParentBean(boolean dynamic) {

    this(null, dynamic, null);
  }

  public TestAdvancedParentBean(AbstractBean writable, boolean dynamic) {

    this(writable, dynamic, null);
  }

  public TestAdvancedParentBean(boolean dynamic, BeanClass type) {

    this(null, dynamic, type);
  }

  public TestAdvancedParentBean(AbstractBean writable, boolean dynamic, BeanClass type) {

    super(writable, dynamic, type);
    this.Name = add(new StringProperty("Name"));
  }

  @Override
  protected AbstractBean create(AbstractBean writableBean, boolean dynamicFlag) {

    return new TestAdvancedParentBean(writableBean, dynamicFlag, getType());
  }

}
