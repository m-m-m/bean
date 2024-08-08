/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.operation;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;

/**
 * {@link BeanOperation} for {@link WritableBean#equals(Object)}.
 *
 * @since 1.0.0
 */
public class BeanOperationEquals extends BeanOperation {

  /**
   * The constructor.
   */
  public BeanOperationEquals() {

    super();
  }

  @Override
  public Object invoke(BeanProxy proxy, Object[] args) throws Throwable {

    if ((args.length == 1) && proxy.getProxy() == args[0]) {
      return Boolean.TRUE;
    }
    return Boolean.FALSE;
  }

}
