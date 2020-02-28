/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.test;

import java.time.LocalDate;
import java.time.Period;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.property.number.integers.IntegerProperty;

/**
 * Test of {@link BeanFactory}.
 */
public class BeanFactoryTest extends Assertions {

  /** Test of {@link BeanFactory#create(Class)} from an interface. */
  @Test
  public void testCreateFromInterface() {

    PersonBean bean = BeanFactory.get().create(PersonBean.class);
    assertThat(bean).isNotNull();
    assertThat(bean.Name().getName()).isEqualTo("Name");
    assertThat(bean.Name().get()).isNull();
    String name = "John Doe";
    bean.setName(name);
    assertThat(bean.getName()).isEqualTo(name);
    assertThat(bean.Name().get()).isEqualTo(name);
    assertThat(bean.Age().getName()).isEqualTo("Age");
    assertThat(bean.Age().get()).isNull();
    Integer age = Integer.valueOf(42);
    bean.setAge(age);
    assertThat(bean.getAge()).isEqualTo(age);
    assertThat(bean.Age().get()).isEqualTo(age);
  }

  /** Test of {@link BeanFactory#create(Class)} from an interface with inheritance. */
  @Test
  public void testCreateFromInterfaceWithInheritance() {

    ContactBean bean = BeanFactory.get().create(ContactBean.class);
    String name = "John Doe";
    bean.setName(name);
    int age = 20;
    IntegerProperty ageProperty = bean.Age();
    LocalDate bithday = LocalDate.now().minus(Period.ofYears(age));
    assertThat(ageProperty.get()).isNull();
    bean.setBirthday(bithday);
    assertThat(ageProperty.get()).isEqualTo(age);
  }

  /** Test of {@link BeanFactory#create(Class)} with a bean class implementation. */
  @Test
  public void testCreateFromClass() {

    TestBean bean = BeanFactory.get().create(TestBean.class);
    assertThat(bean.getClass()).isSameAs(TestBean.class);
    String name = "John Doe";
    bean.setName(name);
    int age = 20;
    bean.Age.setValue(age);
    assertThat(bean.getName()).isEqualTo(name);
    assertThat(bean.getAge()).isEqualTo(age);
  }

}
