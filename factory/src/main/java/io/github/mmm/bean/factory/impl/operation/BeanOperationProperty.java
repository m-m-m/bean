/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.operation;

import java.lang.invoke.MethodHandle;
import java.lang.reflect.Method;

import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.property.WritableProperty;

/**
 * {@link BeanOperation} for property accessor method.
 *
 * @since 1.0.0
 */
public class BeanOperationProperty extends BeanOperationOnProperty {

  /**
   * The constructor.
   *
   * @param propertyName the {@link #getPropertyName() propertyName}.
   * @param method the {@link #getMethod() method}.
   */
  public BeanOperationProperty(String propertyName, Method method) {

    super(propertyName, method);
  }

  @Override
  public Object invoke(BeanProxy proxy, Object[] args) throws Throwable {

    return proxy.getBean().getProperty(this.propertyName);
  }

  @Override
  public WritableProperty<?> createProperty(BeanProxy proxy) {

    if (this.method.isDefault()) {
      try {
        MethodHandle handle = createMethodHandle(this.method);
        if (handle != null) {
          return (WritableProperty<?>) handle.invoke(proxy.getProxy());
        }
      } catch (Throwable e) {
        throw new IllegalStateException(
            "Failed to create property " + this.propertyName + " from default method " + this.method, e);
      }
    }
    return createProperty(proxy, this.method.getReturnType(), null);
  }

}
