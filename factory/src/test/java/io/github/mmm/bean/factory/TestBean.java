/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.AdvancedBean;
import io.github.mmm.bean.Bean;
import io.github.mmm.bean.Name;
import io.github.mmm.property.number.integers.IntegerProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * A {@link Bean} for testing.
 */
@Name("mmm.TestBean")
public class TestBean extends AdvancedBean implements PersonBean {

  /** Full name of person. */
  public final StringProperty Name;

  /** Age of person. */
  public final IntegerProperty Age;

  /**
   * The constructor.
   */
  public TestBean() {

    this(null, false);
  }

  /**
   * The constructor.
   *
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   */
  public TestBean(boolean dynamic) {

    this(null, dynamic);
  }

  /**
   * The constructor.
   *
   * @param writable the writable {@link Bean} to create a {@link #isReadOnly() read-only} view on or {@code null} to
   *        create a regular mutable {@link Bean}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   */
  public TestBean(AbstractBean writable, boolean dynamic) {

    super(writable, dynamic);
    this.Name = add(new StringProperty("Name"));
    this.Age = add(new IntegerProperty("Age"));
  }

  @Override
  protected AbstractBean create(AbstractBean writable, boolean dynamic) {

    return new TestBean(writable, dynamic);
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
