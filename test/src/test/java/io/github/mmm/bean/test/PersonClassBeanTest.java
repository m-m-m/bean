/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.test;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.bean.BeanFactory;

/**
 * Test of {@link PersonClassBean}.
 */
public class PersonClassBeanTest extends Assertions {

  /** Test of {@link PersonClassBean}. */
  @Test
  public void testCreateFromClass() {

    PersonClassBean bean = BeanFactory.get().create(PersonClassBean.class);
    assertThat(bean.getClass()).isSameAs(PersonClassBean.class);
    String name = "John Doe";
    bean.setName(name);
    int age = 20;
    bean.Age.setValue(age);
    assertThat(bean.getName()).isEqualTo(name);
    assertThat(bean.getAge()).isEqualTo(age);
  }

}
