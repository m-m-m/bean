/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.examples;

import io.github.mmm.bean.Bean;
import io.github.mmm.bean.BeanName;
import io.github.mmm.property.container.list.ListProperty;
import io.github.mmm.property.number.integers.IntegerProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * A {@link Bean} for testing.
 */
@BeanName("mmm_TestBuildersBean")
public class TestBuildersBean extends Bean {

  /** Full name of person. */
  public final StringProperty Name;

  /** Age of person. */
  public final IntegerProperty Age;

  /** The list of hobbies of person. */
  public final ListProperty<String> Hobbies;

  /**
   * The constructor.
   */
  public TestBuildersBean() {

    super();
    this.Name = add().newString().withValidator().mandatory().and().build("Name");
    this.Age = add().newInteger().withValidator().range(0, 150).and().build("Age");
    this.Hobbies = add().newString().withValidator().mandatory().pattern("[a-zA-Z ]").and().asList().build("Hobbies");
  }

  @Override
  public boolean isDynamic() {

    return true;
  }

}
