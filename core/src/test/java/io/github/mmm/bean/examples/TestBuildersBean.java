/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.examples;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.Bean;
import io.github.mmm.bean.Name;
import io.github.mmm.property.container.list.ListProperty;
import io.github.mmm.property.number.integers.IntegerProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * A {@link Bean} for testing.
 */
@Name("mmm.TestBuildersBean")
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

    this(null, false);
  }

  /**
   * The constructor.
   *
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   */
  public TestBuildersBean(boolean dynamic) {

    this(null, dynamic);
  }

  /**
   * The constructor.
   *
   * @param writable the writable {@link Bean} to create a {@link #isReadOnly() read-only} view on or {@code null} to
   *        create a regular mutable {@link Bean}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   */
  public TestBuildersBean(AbstractBean writable, boolean dynamic) {

    super(writable, dynamic);
    this.Name = add().newString().withValidator().mandatory().and().build("Name");
    this.Age = add().newInteger().withValidator().range(0, 150).and().build("Age");
    this.Hobbies = add().newString().withValidator().mandatory().pattern("[a-zA-Z ]").and().asList().build("Hobbies");
  }

  @Override
  protected AbstractBean create(AbstractBean writable, boolean dynamic) {

    return new TestBuildersBean(writable, dynamic);
  }

}
