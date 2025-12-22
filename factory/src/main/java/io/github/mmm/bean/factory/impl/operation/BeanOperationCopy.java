/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.operation;

import io.github.mmm.bean.BeanHelper;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.factory.impl.bean.InternalBean;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.bean.factory.impl.proxy.BeanProxyInstance;

/**
 * {@link BeanOperation} for {@link ReadableBean#copy()}.
 *
 * @since 1.0.0
 */
public class BeanOperationCopy extends BeanOperation {

  /**
   * The constructor.
   */
  public BeanOperationCopy() {

    super();
  }

  @Override
  public Object invoke(BeanProxy proxy, Object[] args) throws Throwable {

    BeanProxyInstance instance = proxy.getPrototype().newInstance();
    InternalBean copy = instance.getBean();
    BeanHelper.copy(proxy.getBean(), copy);
    return instance.getProxy();
  }

}
