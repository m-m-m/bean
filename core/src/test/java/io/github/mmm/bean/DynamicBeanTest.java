/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import org.junit.jupiter.api.Test;

import io.github.mmm.bean.examples.TestBean;
import io.github.mmm.property.WritableProperty;

/**
 * Test of {@link DynamicBean}.
 */
public class DynamicBeanTest extends AbstractBeanTest {

  /**
   * Test of {@link TestBean}.
   */
  @Test
  public void testBean() {

    DynamicBean bean = new DynamicBean();
    assertThat(bean.isPrototype()).isFalse();
    assertThat(bean.isDynamic()).isTrue();
    assertThat(bean.isReadOnly()).isFalse();
    assertThat(bean.getPropertyCount()).isEqualTo(0);
    checkType(bean, "mmm.DynamicBean");
    assertThat(bean.getPropertyNameForAlias("Name")).isNull();
    DynamicBean readOnly = WritableBean.getReadOnly(bean);
    assertThat(readOnly.getPropertyNameForAlias("Name")).isNull();

    WritableProperty<String> nameProperty = bean.getOrCreateProperty("Name", String.class);
    checkProperty(bean, nameProperty, "John Doe");
    WritableProperty<Integer> ageProperty = bean.getOrCreateProperty("Age", Integer.class);
    checkProperty(bean, ageProperty, Integer.valueOf(42));
    WritableProperty<Bean> beanProperty = bean.getOrCreateProperty("Bean", Bean.class);
    checkProperty(bean, beanProperty, new Bean());
  }

}
