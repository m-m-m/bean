/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.generator.test;

import java.time.LocalDate;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.AdvancedBean;
import io.github.mmm.bean.BeanClass;
import io.github.mmm.property.number.integers.IntegerProperty;
import io.github.mmm.property.string.StringProperty;
import io.github.mmm.property.temporal.localdate.LocalDateProperty;

/**
 * Manual implementation of {@link ContactBean} to compare with generated code and ensure compilation works as expected.
 */
public class ContactBeanImpl extends AdvancedBean implements ContactBean {

  private final LocalDateProperty Birthday;

  private final StringProperty Name;

  private final IntegerProperty Age;

  /**
   * The constructor.
   *
   * @param type - the {@link #getType() type}.
   */
  public ContactBeanImpl(BeanClass type) {

    super(type);
    this.Birthday = add().newLocalDate("Birthday");
    this.Name = add().newString("Name");
    this.Age = add(ContactBean.super.Age());
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
  public IntegerProperty Age() {

    return this.Age;
  }

  @Override
  public Integer getAge() {

    return this.Age.get();
  }

  @Override
  protected AbstractBean create() {

    return new ContactBeanImpl(getType());
  }

}
