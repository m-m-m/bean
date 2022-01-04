/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.test;

import java.time.LocalDate;
import java.time.Period;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.base.exception.ReadOnlyException;
import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.test.ContactBean;
import io.github.mmm.bean.test.PersonBean;
import io.github.mmm.bean.test.TestBean;
import io.github.mmm.property.number.integers.IntegerProperty;

/**
 * Test of {@link BeanFactory}[Impl].
 */
public class BeanFactoryImplTest extends Assertions {

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
    PersonBean copy = ReadableBean.copyReadOnly(bean);
    assertThat(copy.Age().get()).isEqualTo(age);
    assertThat(copy.Name().get()).isEqualTo(name);
    try {
      copy.Age().set(99);
      failBecauseExceptionWasNotThrown(ReadOnlyException.class);
    } catch (ReadOnlyException e) {
      assertThat(e.getNlsMessage().getMessage())
          .isEqualTo("Failed to modify read-only object: Property Age is readonly and cannot be modified.");
    }
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
