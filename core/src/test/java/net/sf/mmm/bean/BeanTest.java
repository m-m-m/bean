/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

import java.time.LocalDate;

import net.sf.mmm.bean.BeanType;
import net.sf.mmm.bean.WritableBean;
import net.sf.mmm.property.temporal.localdate.LocalDateProperty;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link net.sf.mmm.bean.Bean} via {@link TestBean}.
 */
public class BeanTest extends Assertions {

  /**
   * Test of {@link TestBean}.
   */
  @Test
  public void testBean() {

    TestBean bean = new TestBean();
    assertThat(bean.isClass()).isFalse();
    assertThat(bean.isDynamic()).isFalse();
    assertThat(bean.isReadOnly()).isFalse();
    assertThat(bean.Name.getName()).isEqualTo("Name");
    assertThat(bean.Name.getValue()).isNull();
    assertThat(bean.Age.getName()).isEqualTo("Age");
    assertThat(bean.Age.getValue()).isNull();
    assertThat(bean.getProperty("Name")).isSameAs(bean.Name);
    assertThat(bean.getProperty("Age")).isSameAs(bean.Age);
    assertThat(bean.getPropertyCount()).isEqualTo(2);
    BeanType type = bean.getType();
    assertThat(type.getJavaClass()).isSameAs(TestBean.class);
    assertThat(type.getPackageName()).isEqualTo(TestBean.class.getPackageName());
    assertThat(type.getSimpleName()).isEqualTo(TestBean.class.getSimpleName());
    assertThat(type.getQualifiedName()).isEqualTo(TestBean.class.getName());
    assertThat(type.getStableName()).isEqualTo("mmm.TestBean");
    assertThat(bean.getPropertyNameForAlias("Undefined")).isNull();
    TestBean readOnly = WritableBean.getReadOnly(bean);
    assertThat(readOnly.getRequiredProperty("Name")).isSameAs(readOnly.Name).isNotSameAs(bean.Name)
        .isEqualTo(bean.Name);
    assertThat(readOnly.getRequiredProperty("Name").isReadOnly()).isTrue();
    assertThat(readOnly.getRequiredProperty("Age")).isSameAs(readOnly.Age).isNotSameAs(bean.Age).isEqualTo(bean.Age);
    assertThat(readOnly.getRequiredProperty("Age").isReadOnly()).isTrue();
    String name = "John Doe";
    bean.Name.set(name);
    assertThat(readOnly.Name.get()).isSameAs(name);
    int age = 42;
    bean.Age.set(age);
    assertThat(readOnly.Age.get()).isEqualTo(age);
  }

  /**
   * Test of {@link TestBean} with {@link TestBean#isDynamic() dynamic flag} set.
   */
  @Test
  public void testBeanDynamic() {

    TestBean bean = new TestBean(true);
    assertThat(bean.isClass()).isFalse();
    assertThat(bean.isDynamic()).isTrue();
    assertThat(bean.isReadOnly()).isFalse();
    assertThat(bean.Name.getName()).isEqualTo("Name");
    assertThat(bean.Name.getValue()).isNull();
    assertThat(bean.Age.getName()).isEqualTo("Age");
    assertThat(bean.Age.getValue()).isNull();
    assertThat(bean.getProperty("Name")).isSameAs(bean.Name);
    assertThat(bean.getProperty("Age")).isSameAs(bean.Age);
    assertThat(bean.getPropertyCount()).isEqualTo(2);
    TestBean readOnly = WritableBean.getReadOnly(bean);
    assertThat(readOnly.getRequiredProperty("Name")).isSameAs(readOnly.Name).isNotSameAs(bean.Name)
        .isEqualTo(bean.Name);
    assertThat(readOnly.getRequiredProperty("Name").isReadOnly()).isTrue();
    assertThat(readOnly.getRequiredProperty("Age")).isSameAs(readOnly.Age).isNotSameAs(bean.Age).isEqualTo(bean.Age);
    assertThat(readOnly.getRequiredProperty("Age").isReadOnly()).isTrue();
    String name = "John Doe";
    bean.Name.set(name);
    assertThat(readOnly.Name.get()).isSameAs(name);
    int age = 42;
    bean.Age.set(age);
    assertThat(readOnly.Age.get()).isEqualTo(age);
    assertThat(readOnly.getPropertyCount()).isEqualTo(2);
    LocalDateProperty birthday = bean.addProperty(new LocalDateProperty("Birthday"));
    assertThat(bean.getPropertyCount()).isEqualTo(3);
    assertThat(bean.getProperty("Birthday")).isSameAs(birthday);
    assertThat(readOnly.getProperties()).hasSize(3);
    assertThat(readOnly.getPropertyCount()).isEqualTo(3);
    assertThat(readOnly.getProperty("Birthday")).isNotSameAs(birthday).isEqualTo(birthday);
    assertThat(readOnly.getProperty("Birthday").isReadOnly()).isTrue();
    // yes, this is inconsistent and does not match the age, it is only a test
    LocalDate date = LocalDate.of(2003, 02, 01);
    birthday.set(date);
    assertThat(readOnly.getProperty("Birthday").getValue()).isSameAs(date);
  }

}
