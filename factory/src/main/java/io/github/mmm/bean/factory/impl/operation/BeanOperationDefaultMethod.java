/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.operation;

import java.lang.reflect.Method;

import io.github.mmm.bean.factory.impl.proxy.BeanProxy;

/**
 * Operation for {@link Method#isDefault() default} {@link Method}s.
 *
 * @since 1.0.0
 */
public class BeanOperationDefaultMethod extends BeanOperation {

  private final Method method;

  /**
   * The constructor.
   *
   * @param method the the {@link Method#isDefault() default} {@link Method}.
   */
  public BeanOperationDefaultMethod(Method method) {

    super();
    assert method.isDefault();
    this.method = method;
  }

  @Override
  public Object invoke(BeanProxy proxy, Object[] args) throws Throwable {

    return invokeDefaultMethod(proxy, this.method, args);
  }

}
