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
   * @param prototype the {@link #getPrototype() prototype}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   */
  public BeanProxyInstance(BeanProxyPrototype prototype, boolean dynamic) {

    super(prototype.beanFactory, prototype.beanType, dynamic, prototype.interfaces);
    this.prototype = prototype;
    prototype.initProperties(this);
  }

  @Override
  public BeanProxyPrototype getPrototype() {

    return this.prototype;
  }

}
