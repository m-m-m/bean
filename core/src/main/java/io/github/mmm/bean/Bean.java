/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import io.github.mmm.bean.impl.BeanTypeImpl;

/**
 * Regular implementation of {@link WritableBean}. For {@link VirtualBean}s use {@link AdvancedBean} instead.
 */
public class Bean extends AbstractBean {

  private final BeanType type;

  /**
   * The constructor.
   */
  public Bean() {

    super();
    this.type = BeanTypeImpl.asType(getClass());
  }

  @Override
  public BeanType getType() {

    return this.type;
  }

  @Override
  public final boolean isPrototype() {

    return false;
  }

}
