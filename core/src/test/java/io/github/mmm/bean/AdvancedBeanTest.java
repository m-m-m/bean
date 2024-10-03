/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.time.LocalDate;
import java.util.Collection;

import org.junit.jupiter.api.Test;

import io.github.mmm.bean.examples.TestAdvancedBean;
import io.github.mmm.bean.examples.TestAdvancedParentBean;
import io.github.mmm.bean.examples.TestBean;
import io.github.mmm.property.string.StringProperty;
import io.github.mmm.property.time.localdate.LocalDateProperty;

/**
 * Test of {@link AbstractBean} via {@link TestAdvancedBean}.
 */
public class AdvancedBeanTest extends AbstractBeanTest {
  /**
   * Test of {@link TestBean} with {@link TestBean#isDynamic() dynamic flag} set.
   */
  @Test
  public void testAdvancedBeanDynamicVirtual() {

    // class
    BeanClass virtucalBeanClass = BeanClass.createVirtual("com.foo", "MyVirtualBean", "foo_VirtualBean",
        TestAdvancedBean.PROTOTYPE.getType());
    assertThat(virtucalBeanClass.getQualifiedName()).isEqualTo("com.foo.MyVirtualBean");
    assertThat(virtucalBeanClass.getStableName()).isEqualTo("foo_VirtualBean");
    Collection<BeanClass> superClasses = virtucalBeanClass.getSuperClasses();
    assertThat(superClasses).hasSize(1);
    BeanClass testAdvancedBeanClass = superClasses.iterator().next();
    assertThat(testAdvancedBeanClass.getQualifiedName()).isEqualTo(TestAdvancedBean.class.getName());
    assertThat(testAdvancedBeanClass.getStableName()).isEqualTo("mmm_TestAdvancedBean");
    superClasses = testAdvancedBeanClass.getSuperClasses();
    assertThat(superClasses).hasSize(1);
    BeanClass testAdvancedParentBeanClass = superClasses.iterator().next();
    assertThat(testAdvancedParentBeanClass.getQualifiedName()).isEqualTo(TestAdvancedParentBean.class.getName());
    assertThat(testAdvancedParentBeanClass.getStableName()).isEqualTo("mmm_TestAdvancedParentBean");

    // bean
    // first instance created for virtual bean class is the prototype
    TestAdvancedBean prototype = new TestAdvancedBean(virtucalBeanClass);
    assertThat(prototype.isPrototype()).isTrue();
    assertThat(prototype.getType()).isSameAs(virtucalBeanClass);
    assertThat(virtucalBeanClass.getPrototype()).isSameAs(prototype);
    TestAdvancedBean bean = new TestAdvancedBean(virtucalBeanClass);
    assertThat(bean.isPrototype()).isFalse();
    assertThat(bean.isDynamic()).isTrue();
    assertThat(bean.isReadOnly()).isFalse();
    assertThat(bean.getType()).isSameAs(virtucalBeanClass);
    assertThat(bean.Name.getName()).isEqualTo("Name");
    assertThat(bean.Name.getValue()).isNull();
    assertThat(bean.Age.getName()).isEqualTo("Age");
    assertThat(bean.Age.get()).isNull();
    assertThat(bean.getProperty("Name")).isSameAs(bean.Name);
    assertThat(bean.getProperty("Age")).isSameAs(bean.Age);
    assertThat(bean.getPropertyCount()).isEqualTo(2);
    assertThat(virtucalBeanClass.getPrototype().getPropertyCount()).isEqualTo(2);
    assertThat(BeanTestHelper.getPropertyNames(virtucalBeanClass.getPrototype())).containsExactlyInAnyOrder("Name",
        "Age");
    assertThat(testAdvancedBeanClass.getPrototype().getPropertyCount()).isEqualTo(2);
    String name = "John Doe";
    bean.Name.set(name);
    int age = 42;
    bean.Age.set(age);
    LocalDateProperty birthday = bean.addProperty(new LocalDateProperty("Birthday"));
    assertThat(bean.getPropertyCount()).isEqualTo(3);
    assertThat(bean.getProperty("Birthday")).isSameAs(birthday);
    // yes, this is inconsistent and does not match the age, it is only a test
    LocalDate date = LocalDate.of(2003, 02, 01);
    birthday.set(date);
    TestAdvancedBean readOnly = WritableBean.getReadOnly(bean);
    assertThat(readOnly.getRequiredProperty("Name")).isSameAs(readOnly.Name).isNotSameAs(bean.Name)
        .isEqualTo(bean.Name);
    assertThat(readOnly.getRequiredProperty("Name").isReadOnly()).isTrue();
    assertThat(readOnly.getRequiredProperty("Age")).isSameAs(readOnly.Age).isNotSameAs(bean.Age).isEqualTo(bean.Age);
    assertThat(readOnly.getRequiredProperty("Age").isReadOnly()).isTrue();
    assertThat(readOnly.Name.get()).isSameAs(name);
    assertThat(readOnly.Age.get()).isEqualTo(age);
    assertThat(readOnly.getProperties()).hasSize(3);
    assertThat(readOnly.getPropertyCount()).isEqualTo(3);
    assertThat(readOnly.getProperty("Birthday")).isNotSameAs(birthday).isEqualTo(birthday);
    assertThat(readOnly.getProperty("Birthday").isReadOnly()).isTrue();
    assertThat(readOnly.getProperty("Birthday").get()).isSameAs(date);
    StringProperty string = testAdvancedParentBeanClass.getPrototype().addProperty(new StringProperty("String"));
    string.set("defaultValue");
    assertThat(readOnly.getProperties()).hasSize(4);
    assertThat(readOnly.getProperty("String")).isEqualTo(string);
    String newStringValue = "value";
    bean.set("String", newStringValue);
    String value = bean.get("String");
    assertThat(value).isEqualTo(newStringValue);
    value = readOnly.get("String");
    assertThat(value).isEqualTo(newStringValue);
    bean = new TestAdvancedBean(virtucalBeanClass);
    value = bean.get("String");
    assertThat(value).isEqualTo("defaultValue");
  }

}
