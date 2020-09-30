/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;

import io.github.mmm.bean.examples.TestBean;
import io.github.mmm.bean.examples.TestBuildersBean;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.temporal.localdate.LocalDateProperty;
import io.github.mmm.validation.ValidationResult;
import io.github.mmm.value.observable.ObservableEventReceiver;

/**
 * Test of {@link io.github.mmm.bean.Bean} via {@link TestBean}.
 */
public class BeanTest extends AbstractBeanTest {

  /**
   * Test of {@link TestBean}.
   */
  @Test
  public void testBean() {

    TestBean bean = new TestBean();
    assertThat(bean.isPrototype()).isFalse();
    assertThat(bean.isDynamic()).isFalse();
    assertThat(bean.isReadOnly()).isFalse();
    checkType(bean, "mmm.TestBean");
    assertThat(bean.Name.getName()).isEqualTo("Name");
    assertThat(bean.Name.getValue()).isNull();
    checkProperty(bean, bean.Name, "John Doe");
    TestBean bean2 = new TestBean();
    for (WritableProperty<?> property : bean.getProperties()) {
      bean2.set(property.getName(), property.get());
    }
    assertThat(bean.Age.getName()).isEqualTo("Age");
    assertThat(bean.Age.get()).isNull();
    checkProperty(bean, bean.Age, Integer.valueOf(42));
    assertThat(bean.getPropertyCount()).isEqualTo(2);
    assertThat(bean.getPropertyNameForAlias("Undefined")).isNull();
  }

  /**
   * Test of {@link TestBean} with {@link TestBean#isDynamic() dynamic flag} set.
   */
  @Test
  public void testBeanDynamic() {

    TestBean bean = new TestBean(true);
    assertThat(bean.isPrototype()).isFalse();
    assertThat(bean.isDynamic()).isTrue();
    assertThat(bean.isReadOnly()).isFalse();
    assertThat(bean.Name.getName()).isEqualTo("Name");
    assertThat(bean.Name.getValue()).isNull();
    assertThat(bean.Age.getName()).isEqualTo("Age");
    assertThat(bean.Age.get()).isNull();
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
    assertThat(readOnly.getProperty("Birthday").get()).isSameAs(date);
    // change listener
    ObservableEventReceiver<Object> listener = new ObservableEventReceiver<>();
    bean.Age.addListener(listener);
    int newAge = 200;
    bean.Age.set(newAge);
    assertThat(listener.getEvent().getOldValue()).isEqualTo(age);
    assertThat(listener.getEvent().getValue()).isEqualTo(newAge);
  }

  /**
   * Test of {@link TestBuildersBean} using {@link Bean#add()}.
   */
  @Test
  public void testBuilders() {

    TestBuildersBean bean = new TestBuildersBean(true);
    assertThat(bean.isPrototype()).isFalse();
    assertThat(bean.isDynamic()).isTrue();
    assertThat(bean.isReadOnly()).isFalse();
    assertThat(bean.Name.getName()).isEqualTo("Name");
    assertThat(bean.Name.getValue()).isNull();
    assertThat(bean.getProperty("Name")).isSameAs(bean.Name);
    assertThat(bean.Age.getName()).isEqualTo("Age");
    assertThat(bean.Age.get()).isNull();
    assertThat(bean.getProperty("Age")).isSameAs(bean.Age);
    assertThat(bean.Hobbies.getName()).isEqualTo("Hobbies");
    assertThat(bean.Hobbies.get()).isNull();
    assertThat(bean.getProperty("Hobbies")).isSameAs(bean.Hobbies);
    assertThat(bean.getPropertyCount()).isEqualTo(3);
    TestBuildersBean readOnly = WritableBean.getReadOnly(bean);
    assertThat(readOnly.getRequiredProperty("Name")).isSameAs(readOnly.Name).isNotSameAs(bean.Name)
        .isEqualTo(bean.Name);
    assertThat(readOnly.getRequiredProperty("Name").isReadOnly()).isTrue();
    assertThat(readOnly.getRequiredProperty("Age")).isSameAs(readOnly.Age).isNotSameAs(bean.Age).isEqualTo(bean.Age);
    assertThat(readOnly.getRequiredProperty("Age").isReadOnly()).isTrue();
    String name = "John Doe";
    bean.Name.set(name);
    assertThat(readOnly.Name.get()).isSameAs(name);
    int age = 42;
    bean.Age.setValue(age);
    ValidationResult result = bean.validate();
    assertThat(result.isValid()).as(result.toString()).isTrue();

    assertThat(readOnly.Age.get()).isEqualTo(age);
    assertThat(readOnly.getPropertyCount()).isEqualTo(3);
    LocalDateProperty birthday = bean.addProperty(new LocalDateProperty("Birthday"));
    assertThat(bean.getProperty("Birthday")).isSameAs(birthday);
    assertThat(readOnly.getProperties()).hasSize(4);
    assertThat(readOnly.getPropertyCount()).isEqualTo(4);
    assertThat(readOnly.getProperty("Birthday")).isNotSameAs(birthday).isEqualTo(birthday);
    assertThat(readOnly.getProperty("Birthday").isReadOnly()).isTrue();
    // yes, this is inconsistent and does not match the age, it is only a test
    LocalDate date = LocalDate.of(2003, 02, 01);
    birthday.set(date);
    assertThat(readOnly.getProperty("Birthday").get()).isSameAs(date);
    // change listener
    ObservableEventReceiver<Object> listener = new ObservableEventReceiver<>();
    bean.Age.addListener(listener);
    int newAge = 200;
    bean.Age.setValue(newAge);
    assertThat(listener.getEvent().getOldValue()).isEqualTo(age);
    assertThat(listener.getEvent().getValue()).isEqualTo(newAge);
    bean.Name.set(null);
    bean.Hobbies.getOrCreate().add(null);
    result = bean.validate();
    assertThat(result.getMessage(true)).isEqualTo("mmm.TestBuildersBean: [Composed]\n" + //
        "  Hobbies: [Composed]\n" + //
        "    #0: [Mandatory] The value has to be filled.\n" + //
        "  Age: [Range] The value needs to be in the range from 0 to 150.\n" + //
        "  Name: [Mandatory] The value has to be filled.");
  }

}
