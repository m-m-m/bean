/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.operation;

import java.lang.reflect.Method;

import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.property.WritableProperty;

/**
 * {@link BeanOperation} for legacy POJO getter method.
 *
 * @since 1.0.0
 */
public class BeanOperationGetter extends BeanOperationOnProperty {

  private final Object defaultResult;

  /**
   * The constructor.
   *
   * @param propertyName the {@link #getPropertyName() propertyName}.
   * @param method the {@link #getMethod() method}.
   */
  public BeanOperationGetter(String propertyName, Method method) {

    super(propertyName, method);
    this.defaultResult = getDefaultResult(method);
  }

  private static Object getDefaultResult(Method method) {

    Class<?> returnType = method.getReturnType();
    if (returnType.isPrimitive()) {
      if (returnType == boolean.class) {
        return Boolean.FALSE;
      } else if (returnType == long.class) {
        return Long.valueOf(0);
      } else if (returnType == int.class) {
        return Integer.valueOf(0);
      } else if (returnType == double.class) {
        return Double.valueOf(0);
      } else if (returnType == float.class) {
        return Float.valueOf(0);
      } else if (returnType == short.class) {
        return Short.valueOf((short) 0);
      } else if (returnType == byte.class) {
        return Byte.valueOf((byte) 0);
      } else if (returnType == char.class) {
        return Character.valueOf('\0');
      }
    }
    return null;
  }

  @Override
  public Object invoke(BeanProxy proxy, Object[] args) throws Throwable {

    Object value = proxy.getBean().get(this.propertyName);
    if (value == null) {
      value = this.defaultResult;
    }
    return value;
  }

  @Override
  public WritableProperty<?> createProperty(BeanProxy proxy) {

    return createProperty(proxy, null, this.method.getReturnType());
  }

}
