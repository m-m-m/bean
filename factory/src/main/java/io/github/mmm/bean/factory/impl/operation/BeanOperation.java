/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.operation;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import io.github.mmm.bean.BeanHelper;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.bean.factory.impl.proxy.BeanProxyPrototype;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.factory.PropertyFactory;

/**
 * Operation on a {@link BeanProxy}.
 *
 * @since 1.0.0
 */
public abstract class BeanOperation {

  /**
   * The constructor.
   */
  public BeanOperation() {

    super();
  }

  /**
   * Invokes this operation of the given {@code access} using the given arguments.
   *
   * @param proxy the {@link BeanProxy}.
   * @param args the arguments of the reflective method invocation.
   * @return the result of the invocation.
   * @throws Throwable if something goes wrong.
   */
  public abstract Object invoke(BeanProxy proxy, Object[] args) throws Throwable;

  /**
   * @return {@code true} if the {@link WritableProperty} is required for this operation, {@code false} otherwise.
   */
  public boolean isPropertyRequired() {

    return false;
  }

  /**
   * @return {@link io.github.mmm.property.Property#getName() property name}. May be {@code null} if not a property
   *         related method.
   */
  public String getPropertyName() {

    return null;
  }

  /**
   * @param proxy the {@link BeanProxy}.
   * @return a new {@link WritableProperty} for this operation. May be {@code null} if not a property related method.
   */
  public WritableProperty<?> createProperty(BeanProxy proxy) {

    return null;
  }

  /**
   * @param method the {@link Method}.
   * @param prototype the {@link BeanProxyPrototype}.
   * @return the according new {@link BeanOperation}.
   */
  public static BeanOperation of(Method method, BeanProxyPrototype prototype) {

    if (Modifier.isStatic(method.getModifiers())) {
      return null;
    }
    String methodName = method.getName();
    if (method.isDefault()) {
      BeanOperationProperty operation = createOperationProperty(method, prototype);
      if (operation != null) {
        return operation;
      } else {
        return new BeanOperationDefaultMethod(method);
      }
    }
    int parameterCount = method.getParameterCount();
    if (parameterCount == 0) {
      BeanOperationProperty operation = createOperationProperty(method, prototype);
      if (operation != null) {
        return operation;
      } else {
        String propertyName = BeanHelper.getPropertyName4Getter(methodName);
        if (propertyName != null) {
          return new BeanOperationGetter(propertyName, method);
        }
      }
    } else if (parameterCount == 1) {
      String propertyName = BeanHelper.getPropertyName4Setter(methodName);
      if (propertyName != null) {
        return new BeanOperationSetter(propertyName, method);
      }
    }
    return null;
  }

  private static BeanOperationProperty createOperationProperty(Method method, BeanProxyPrototype prototype) {

    String propertyName = BeanHelper.getPropertyName4Property(method);
    if (propertyName == null) {
      return null;
    }
    PropertyFactory<?, ?> factory = BeanHelper.getPropertyFactory(method.getReturnType());
    if (factory != null) {
      return new BeanOperationProperty(propertyName, method);
    }
    return null;
  }

}
