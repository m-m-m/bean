/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

import net.sf.mmm.bean.AbstractBean;
import net.sf.mmm.bean.Bean;
import net.sf.mmm.bean.Name;
import net.sf.mmm.property.number.integers.IntegerProperty;
import net.sf.mmm.property.string.StringProperty;

/**
 * A {@link Bean} for testing.
 */
@Name("mmm.TestBean")
public class TestBean extends Bean {

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

}
