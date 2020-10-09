/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.BeanType;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.bean.impl.BeanTypeImpl;

/**
 * Internal implementation of {@link WritableBean} non virtual instance used from {@link BeanProxy}.
 *
 * @since 1.0.0
 */
public final class SimpleBean extends AbstractBean {

  private final BeanType type;

  /**
   * The constructor.
   *
   * @param type the {@link #getType() type}.
   */
  public SimpleBean(BeanType type) {

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
