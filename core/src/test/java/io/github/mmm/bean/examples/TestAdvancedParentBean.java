/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.examples;

import io.github.mmm.bean.AdvancedBean;
import io.github.mmm.bean.BeanClass;
import io.github.mmm.bean.Name;
import io.github.mmm.property.string.StringProperty;

/**
 * {@link AdvancedBean} for testing.
 */
@SuppressWarnings("javadoc")
@Name("mmm_TestAdvancedParentBean")
public class TestAdvancedParentBean extends AdvancedBean {

  /** @see BeanClass#getPrototype() */
  @SuppressWarnings("hiding")
  public static final TestAdvancedParentBean PROTOTYPE = new TestAdvancedParentBean();

  /** Full name of person. */
  public final StringProperty Name;

  public TestAdvancedParentBean() {

    this(null);
  }

  public TestAdvancedParentBean(BeanClass type) {

    super(type);
    this.Name = add(new StringProperty("Name"));
  }

  @Override
  public boolean isDynamic() {

    return true;
  }

}
