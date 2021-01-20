/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.operation;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

import io.github.mmm.bean.Bean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;

/**
 * Operation for {@link Bean#equals(Object)}.
 *
 * @since 1.0.0
 */
public class BeanOperationDefaultMethod extends BeanOperation {

  private final MethodHandle methodHandle;

  /**
   * The constructor.
   *
   * @param methodHandle the {@link MethodHandle} of the {@link Method#isDefault() default} {@link Method}.
   */
  public BeanOperationDefaultMethod(MethodHandle methodHandle) {

    super();
    this.methodHandle = methodHandle;
  }

  @Override
  public Object invoke(BeanProxy proxy, Object[] args) throws Throwable {

    WritableBean instance = proxy.getProxy();
    if (args == null) {
      return this.methodHandle.invoke(instance);
    } else {
      return this.methodHandle.bindTo(instance).invoke(args);
    }
  }

}
