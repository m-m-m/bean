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
    assertThat(code).isEqualTo("package beanimpl.io.github.mmm.bean.generator.test;\n" //
        + "\n" //
        + "import io.github.mmm.bean.AdvancedBean;\n" //
        + "import io.github.mmm.bean.BeanClass;\n" //
        + "import io.github.mmm.bean.generator.test.ContactBean;\n" //
        + "import io.github.mmm.property.number.integers.IntegerProperty;\n" //
        + "import io.github.mmm.property.string.StringProperty;\n" //
        + "import io.github.mmm.property.temporal.localdate.LocalDateProperty;\n" //
        + "import java.time.LocalDate;\n" //
        + "\n" //
        + "public class ContactBeanImpl extends AdvancedBean implements ContactBean {\n" //
        + "\n" //
        + "  private final IntegerProperty Age;\n" //
        + "  private final LocalDateProperty Birthday;\n" //
        + "  private final StringProperty Name;\n" //
        + "\n" //
        + "  public ContactBeanImpl(BeanClass type) {\n" //
        + "    super(type);\n" //
        + "    this.Age = add(ContactBean.super.Age());\n" //
        + "    this.Birthday = add().newLocalDate().build(\"Birthday\");\n" //
        + "    this.Name = add().newString().build(\"Name\");\n" //
        + "  }\n" //
        + "\n" //
        + "  @Override\n" //
        + "  public IntegerProperty Age() {\n" //
        + "    return this.Age;\n" //
        + "  }\n" //
        + "\n" //
        + "  @Override\n" //
        + "  public Integer getAge() {\n" //
        + "    return this.Age.get();\n" //
        + "  }\n" //
        + "\n" //
        + "  @Override\n" //
        + "  public LocalDateProperty Birthday() {\n" //
        + "    return this.Birthday;\n" //
        + "  }\n" //
        + "\n" //
        + "  @Override\n" //
        + "  public LocalDate getBirthday() {\n" //
        + "    return this.Birthday.get();\n" //
        + "  }\n" //
        + "\n" //
        + "  @Override\n" //
        + "  public void setBirthday(LocalDate birthday) {\n" //
        + "    this.Birthday.set(birthday);\n" //
        + "  }\n" //
        + "\n" //
        + "  @Override\n" //
        + "  public StringProperty Name() {\n" //
        + "    return this.Name;\n" //
        + "  }\n" //
        + "\n" //
        + "  @Override\n" //
        + "  public String getName() {\n" //
        + "    return this.Name.get();\n" //
        + "  }\n" //
        + "\n" //
        + "  @Override\n" //
        + "  public void setName(String name) {\n" //
        + "    this.Name.set(name);\n" //
        + "  }\n" //
        + "\n" //
        + "  @Override\n" //
        + "  protected AbstractBean create() {\n" //
        + "    return new ContactBeanImpl(getType());\n" //
        + "  }\n" //
        + "}\n");
  }

}
