/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.generator;

import java.io.StringWriter;
import java.io.Writer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.bean.generator.test.ContactBean;

/**
 * Test of {@link BeanGenerator}.
 */
public class BeanGeneratorTest extends Assertions {

  /** Test of {@link BeanGenerator#generate(Class, java.io.Writer)} */
  @Test
  public void testGenerateBeanImpl() {

    // given
    Class<ContactBean> beanClass = ContactBean.class;
    BeanGenerator generator = new BeanGenerator();
    Writer writer = new StringWriter();
    // when
    generator.generate(beanClass, writer);
    String code = writer.toString();
    // then
    assertThat(code).isEqualTo("""
        package beanimpl.io.github.mmm.bean.generator.test;

        import io.github.mmm.bean.AbstractBean;
        import io.github.mmm.bean.AdvancedBean;
        import io.github.mmm.bean.BeanClass;
        import io.github.mmm.bean.generator.test.ContactBean;
        import io.github.mmm.property.number.integers.IntegerProperty;
        import io.github.mmm.property.string.StringProperty;
        import io.github.mmm.property.time.localdate.LocalDateProperty;
        import java.time.LocalDate;

        public class ContactBeanImpl extends AdvancedBean implements ContactBean {

          private final IntegerProperty Age;
          private final LocalDateProperty Birthday;
          private final StringProperty Name;

          public ContactBeanImpl(BeanClass type) {
            super(type);
            this.Age = add(ContactBean.super.Age());
            this.Birthday = add().newLocalDate().build("Birthday");
            this.Name = add().newString().build("Name");
          }

          @Override
          public IntegerProperty Age() {
            return this.Age;
          }

          @Override
          public Integer getAge() {
            return this.Age.get();
          }

          @Override
          public LocalDateProperty Birthday() {
            return this.Birthday;
          }

          @Override
          public LocalDate getBirthday() {
            return this.Birthday.get();
          }

          @Override
          public void setBirthday(LocalDate birthday) {
            this.Birthday.set(birthday);
          }

          @Override
          public StringProperty Name() {
            return this.Name;
          }

          @Override
          public String getName() {
            return this.Name.get();
          }

          @Override
          public void setName(String name) {
            this.Name.set(name);
          }

          @Override
          protected AbstractBean create() {
            return new ContactBeanImpl(getType());
          }
        }
        """);
  }

}
