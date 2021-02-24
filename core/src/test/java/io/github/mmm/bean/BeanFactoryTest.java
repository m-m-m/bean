/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import org.junit.jupiter.api.Test;

import io.github.mmm.bean.examples.TestBean;

/**
 * Test of {@link BeanFactory}.
 */
public class BeanFactoryTest extends AbstractBeanTest {

  /** Test of {@link BeanFactory#create(Class)}. */
  @Test
  public void testCreate() {

    BeanFactory factory = BeanFactory.get();

    TestBean bean = factory.create(TestBean.class);
    assertThat(bean.getClass()).isEqualTo(TestBean.class);
    checkType(bean, "mmm_TestBean");
  }

}
