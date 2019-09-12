/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

import java.time.LocalDate;
import java.util.List;

import net.sf.mmm.property.string.StringProperty;
import net.sf.mmm.property.temporal.localdate.LocalDateProperty;

import org.junit.jupiter.api.Test;

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
    BeanClass virtucalBeanClass = BeanClass.createVirtual("com.foo", "MyVirtualBean", "foo.VirtualBean",
        TestAdvancedBean.PROTOTYPE.getType());
    assertThat(virtucalBeanClass.getQualifiedName()).isEqualTo("com.foo.MyVirtualBean");
    assertThat(virtucalBeanClass.getStableName()).isEqualTo("foo.VirtualBean");
    List<BeanClass> superClasses = virtucalBeanClass.getSuperClasses();
    assertThat(superClasses).hasSize(1);
    BeanClass testAdvancedBeanClass = superClasses.get(0);
    assertThat(testAdvancedBeanClass.getQualifiedName()).isEqualTo(TestAdvancedBean.class.getName());
    assertThat(testAdvancedBeanClass.getStableName()).isEqualTo("mmm.TestAdvancedBean");
    superClasses = testAdvancedBeanClass.getSuperClasses();
    assertThat(superClasses).hasSize(1);
    BeanClass testAdvancedParentBeanClass = superClasses.get(0);
    assertThat(testAdvancedParentBeanClass.getQualifiedName()).isEqualTo(TestAdvancedParentBean.class.getName());
    assertThat(testAdvancedParentBeanClass.getStableName()).isEqualTo("mmm.TestAdvancedParentBean");

    // bean
    TestAdvancedBean prototype = new TestAdvancedBean(true, virtucalBeanClass);
    assertThat(prototype.isPrototype()).isTrue();
    assertThat(prototype.getType()).isSameAs(virtucalBeanClass);
    assertThat(virtucalBeanClass.getPrototype()).isSameAs(prototype);
    TestAdvancedBean bean = new TestAdvancedBean(true, virtucalBeanClass);
    assertThat(bean.isPrototype()).isFalse();
    assertThat(bean.isDynamic()).isTrue();
    assertThat(bean.isReadOnly()).isFalse();
    assertThat(bean.getType()).isSameAs(virtucalBeanClass);
    assertThat(bean.Name.getName()).isEqualTo("Name");
    assertThat(bean.Name.getValue()).isNull();
    assertThat(bean.Age.getName()).isEqualTo("Age");
    assertThat(bean.Age.getValue()).isNull();
    assertThat(bean.getProperty("Name")).isSameAs(bean.Name);
    assertThat(bean.getProperty("Age")).isSameAs(bean.Age);
    assertThat(bean.getPropertyCount()).isEqualTo(2);
    assertThat(virtucalBeanClass.getPrototype().getPropertyCount()).isEqualTo(2);
    assertThat(BeanHelper.getPropertyNames(virtucalBeanClass.getPrototype())).containsExactlyInAnyOrder("Name", "Age");
    assertThat(testAdvancedBeanClass.getPrototype().getPropertyCount()).isEqualTo(2);
    TestAdvancedBean readOnly = WritableBean.getReadOnly(bean);
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
    StringProperty string = new StringProperty("String");
    testAdvancedParentBeanClass.getPrototype().addProperty(string);
    assertThat(readOnly.getProperties()).hasSize(4);
    assertThat(readOnly.getProperty("String")).isEqualTo(string);
    bean.set("String", "value");
    virtucalBeanClass.getPrototype().set("String", "defaultValue");
    assertThat(readOnly.get("String")).isEqualTo("value");
    bean = new TestAdvancedBean(true, virtucalBeanClass);
    assertThat(bean.get("String")).isEqualTo("defaultValue");
  }

}
