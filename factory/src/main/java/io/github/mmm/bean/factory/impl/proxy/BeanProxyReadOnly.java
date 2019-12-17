/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.proxy;

import io.github.mmm.bean.WritableBean;

/**
 * {@link BeanProxy} for a regular bean instance (that is not a {@link WritableBean#isPrototype() prototype}).
 *
 * @since 1.0.0
 */
public class BeanProxyReadOnly extends BeanProxyInstance {

  /**
   * The constructor.
   *
   * @param writable the {@link BeanProxyInstanceWritable} to create a read-only view of.
   */
  public BeanProxyReadOnly(BeanProxyInstanceWritable writable) {

    super(writable.prototype, writable.bean, writable.dynamic);
  }

  /**
   * The constructor.
   *
   * @param prototype the {@link BeanProxyPrototype} to create a read-only view of.
   */
  public BeanProxyReadOnly(BeanProxyPrototype prototype) {

    super(prototype, prototype.bean, prototype.dynamic);
  }

  @Override
  public BeanProxy getReadOnly() {

    return this;
  }

}
