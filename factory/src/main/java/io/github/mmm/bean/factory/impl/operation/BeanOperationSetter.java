/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.operation;

import java.lang.reflect.Method;

import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.factory.PropertyFactoryManager;

/**
 * {@link BeanOperation} for legacy POJO setter method.
 *
 * @since 1.0.0
 */
public class BeanOperationSetter extends BeanOperationOnProperty {

  /**
   * The constructor.
   *
   * @param propertyName the {@link #getPropertyName() propertyName}.
   * @param method the {@link #getMethod() method}.
   */
  public BeanOperationSetter(String propertyName, Method method) {

    super(propertyName, method);
  }

  @Override
  public Object invoke(BeanProxy proxy, Object[] args) {

    proxy.getBean().set(this.propertyName, args[0]);
    return null;
  }

  @Override
  public WritableProperty<?> createProperty(BeanProxy proxy) {

    return PropertyFactoryManager.get().create(this.method.getParameterTypes()[0], this.propertyName);
  }

}
