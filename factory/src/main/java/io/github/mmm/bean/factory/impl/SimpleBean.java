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
   * @param writable the writable {@link SimpleBean} to create a {@link #isReadOnly() read-only} view on or {@code null}
   *        to create a regular mutable {@link SimpleBean}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   * @param type the {@link #getType() type}.
   */
  public SimpleBean(AbstractBean writable, boolean dynamic, BeanType type) {

    super(writable, dynamic);
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
