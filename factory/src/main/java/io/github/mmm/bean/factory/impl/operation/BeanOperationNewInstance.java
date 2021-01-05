/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.operation;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;

/**
 * {@link BeanOperation} for {@link ReadableBean#newInstance()}.
 *
 * @since 1.0.0
 */
public class BeanOperationNewInstance extends BeanOperation {

  static final BeanOperationNewInstance INSTANCE = new BeanOperationNewInstance();

  /**
   * The constructor.
   */
  public BeanOperationNewInstance() {

    super();
  }

  @Override
  public Object invoke(BeanProxy proxy, Object[] args) throws Throwable {

    boolean dynamic = proxy.getBean().isDynamic();
    return proxy.getPrototype().newInstance(dynamic).getProxy();
  }

}
