/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.operation;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.bean.factory.impl.proxy.BeanProxyInstance;

/**
 * {@link BeanOperation} for {@link WritableBean#getReadOnly()}.
 *
 * @since 1.0.0
 */
public class BeanOperationGetReadOnly extends BeanOperation {

  /**
   * The constructor.
   */
  public BeanOperationGetReadOnly() {

    super();
  }

  @Override
  public Object invoke(BeanProxy proxy, Object[] args) throws Throwable {

    BeanProxyInstance instance = new BeanProxyInstance(proxy);
    return instance.getProxy();
  }

}
