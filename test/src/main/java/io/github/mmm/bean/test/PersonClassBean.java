/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.test;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.AdvancedBean;
import io.github.mmm.bean.Bean;
import io.github.mmm.bean.Name;
import io.github.mmm.property.number.integers.IntegerProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * A {@link Bean} for testing.
 */
@Name("mmm_TestBean")
public final class PersonClassBean extends AdvancedBean implements PersonBean {

  /** Full name of person. */
  public final StringProperty Name;

  /** Age of person. */
  public final IntegerProperty Age;

  /**
   * The constructor.
   */
  public PersonClassBean() {

    super();
    this.Name = add().newString("Name");
    this.Age = add().newInteger().withValidator().range(0, 99).and().build("Age");
  }

  @Override
  protected AbstractBean create() {

    return new PersonClassBean();
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
  public void setAge(Integer age) {

    this.Age.set(age);
  }

}
