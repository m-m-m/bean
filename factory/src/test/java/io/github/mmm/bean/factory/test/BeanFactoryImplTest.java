/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.test;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.base.exception.ReadOnlyException;
import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.property.string.StringProperty;

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

  /**
   * Test of aliases (via {@link io.github.mmm.bean.PropertyAlias} annotation) from bean interface {@link PersonBean}.
   */
  @Test
  public void testCreateFromInterfaceWithAlias() {

    PersonBean bean = BeanFactory.get().create(PersonBean.class);
    assertThat(bean).isNotNull();
    // name
    StringProperty nameProperty = bean.Name();
    assertThat(nameProperty.getName()).isEqualTo("Name");
    assertThat(bean.getAliases().getName("LegacyName")).isEqualTo("Name");
    assertThat(bean.getAliases().getName("@alternativeName")).isEqualTo("Name");
    assertThat(bean.getProperty("LegacyName")).isSameAs(nameProperty);
    assertThat(bean.getProperty("@alternativeName")).isSameAs(nameProperty);
    assertThat(bean.getAliases().getAliases("Name")).containsExactlyInAnyOrder("LegacyName", "@alternativeName");
    // age
    assertThat(bean.getAliases().getName("IceAge")).isEqualTo("Age");
    assertThat(bean.getAliases().getAliases("Age")).containsExactlyInAnyOrder("IceAge");
    assertThat(bean.getProperty("IceAge")).isSameAs(bean.Age());
  }
}
