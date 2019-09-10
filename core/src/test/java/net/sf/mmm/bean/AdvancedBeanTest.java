/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

import java.time.LocalDate;
import java.util.List;

import net.sf.mmm.property.booleans.BooleanProperty;
import net.sf.mmm.property.temporal.localdate.LocalDateProperty;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * Test of {@link AbstractBean} via {@link TestAdvancedBean}.
 */
public class AdvancedBeanTest extends Assertions {
  /**
   * Test of {@link TestBean} with {@link TestBean#isDynamic() dynamic flag} set.
   */
  @Test
  public void testAdvancedBeanDynamicVirtual() {

    BeanClass beanClass = BeanClass.createVirtual("com.foo", "MyVirtualBean", "foo.VirtualBean", true,
        BeanClass.of(TestAdvancedBean.class));
    assertThat(beanClass.getQualifiedName()).isEqualTo("com.foo.MyVirtualBean");
    assertThat(beanClass.getStableName()).isEqualTo("foo.VirtualBean");
    List<BeanClass> superClasses = beanClass.getSuperClasses();
    assertThat(superClasses).hasSize(1);
    BeanClass superClass = superClasses.get(0);
    assertThat(superClass.getQualifiedName()).isEqualTo(TestAdvancedBean.class.getName());
    superClasses = superClass.getSuperClasses();
    assertThat(superClasses).hasSize(1);
    superClass = superClasses.get(0);
    assertThat(superClass.getQualifiedName()).isEqualTo(AdvancedBean.class.getName());
    TestAdvancedBean bean = new TestAdvancedBean(true, beanClass);
    assertThat(bean.isClass()).isFalse();
    assertThat(bean.isDynamic()).isTrue();
    assertThat(bean.isReadOnly()).isFalse();
    assertThat(bean.getType()).isSameAs(beanClass);
    assertThat(bean.Name.getName()).isEqualTo("Name");
    assertThat(bean.Name.getValue()).isNull();
    assertThat(bean.Age.getName()).isEqualTo("Age");
    assertThat(bean.Age.getValue()).isNull();
    assertThat(bean.getProperty("Name")).isSameAs(bean.Name);
    assertThat(bean.getProperty("Age")).isSameAs(bean.Age);
    assertThat(bean.getPropertyCount()).isEqualTo(2);
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
    BooleanProperty friend = new BooleanProperty("Fried");
    superClass.addProperty(friend);
    assertThat(readOnly.getProperties()).hasSize(4);
    assertThat(readOnly.getProperty("Fried")).isEqualTo(friend);
  }

}
