/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

/**
 * A {@link Bean} that is always {@link #isDynamic() dynamic}. Only exists for simplicity.
 */
@Name("mmm.DynamicBean")
public final class DynamicBean extends Bean {

  /**
   * The constructor.
   */
  public DynamicBean() {

    super(null, true);
  }

  private DynamicBean(AbstractBean writable) {

    super(writable, true);
  }

  @Override
  protected Bean create(AbstractBean writableBean, boolean dynamic) {

    return new DynamicBean(writableBean);
  }

}
