/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.proxy;

import io.github.mmm.bean.WritableBean;

/**
 * {@link BeanProxy} for a regular bean instance (that is not a {@link WritableBean#isPrototype() prototype}).
 *
 * @since 1.0.0
 */
public final class BeanProxyInstance extends BeanProxy {

  /** @see #getPrototype() */
  protected final BeanProxyPrototype prototype;

  /**
   * The constructor.
   *
   * @param proxy the {@link BeanProxy} to create a {@link WritableBean#getReadOnly() read-only} view for.
   */
  public BeanProxyInstance(BeanProxy proxy) {

    this(proxy.proxy, proxy.getPrototype());
  }

  /**
   * The constructor.
   *
   * @param prototype the {@link #getPrototype() prototype}.
   */
  public BeanProxyInstance(BeanProxyPrototype prototype) {

    this(null, prototype);
    prototype.initProperties(this);
  }

  private BeanProxyInstance(WritableBean writable, BeanProxyPrototype prototype) {

    super(prototype.beanFactory, writable, prototype.beanType, prototype.interfaces);
    this.prototype = prototype;
  }

  @Override
  public BeanProxyPrototype getPrototype() {

    return this.prototype;
  }

}
