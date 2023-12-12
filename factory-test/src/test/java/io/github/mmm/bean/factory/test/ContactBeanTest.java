/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.test;

import java.time.LocalDate;
import java.time.Period;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.property.number.integers.IntegerProperty;

/**
 * Test of {@link ContactBean}.
 */
public class ContactBeanTest extends Assertions {

  /** Test of {@link ContactBean}. */
  @Test
  public void testCreateFromInterfaceWithInheritance() {

    ContactBean bean = ContactBean.of();
    String name = "John Doe";
    bean.setName(name);
    int age = 20;
    IntegerProperty ageProperty = bean.Age();
    LocalDate bithday = LocalDate.now().minus(Period.ofYears(age));
    assertThat(ageProperty.get()).isNull();
    assertThat(bean.Age()).isSameAs(ageProperty);
    assertThat(bean.getType().getMetaInfo().get("table")).isEqualTo("CONTACT");
    assertThat(bean.Birthday().getMetadata().getMetaInfo().get("column")).isEqualTo("DATE_OF_BIRTH");
    assertThat(bean.Birthday().getMetadata().getMetaInfo().getAsLong("precision")).isEqualTo(7L);
    bean.setBirthday(bithday);
    assertThat(ageProperty.get()).isEqualTo(age);
    // ensure expression for age property is recreated on copy not pointing to original bean
    ContactBean copy = ReadableBean.copy(bean, false);
    assertThat(copy).isNotSameAs(bean);
    copy.Birthday().set(LocalDate.now());
    assertThat(copy.Age().get()).isEqualTo(0);
    // ensure expression for age property is recreated on newInstance not pointing to original bean
    copy = ReadableBean.newInstance(bean);
    assertThat(copy).isNotSameAs(bean);
    copy.Birthday().set(LocalDate.now());
    assertThat(copy.Age().get()).isEqualTo(0);
  }

}
