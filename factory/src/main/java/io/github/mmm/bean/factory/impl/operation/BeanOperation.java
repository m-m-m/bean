/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.operation;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;

import io.github.mmm.bean.BeanHelper;
import io.github.mmm.bean.PropertyMethod;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.bean.factory.impl.proxy.BeanProxyPrototype;
import io.github.mmm.property.WritableProperty;

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
  public static BeanOperation create(Method method, BeanProxyPrototype prototype) {

    String methodName = method.getName();
    char first = methodName.charAt(0);
    boolean defaultMethod = method.isDefault();
    int parameterCount = method.getParameterCount();
    if (defaultMethod) {
      if ((parameterCount == 0) && method.isAnnotationPresent(PropertyMethod.class)) {
        return new BeanOperationProperty(methodName, method);
      }
      return new BeanOperationDefaultMethod(method);
    }
    if (parameterCount == 0) {
      if (Character.isUpperCase(first)) {
        return new BeanOperationProperty(methodName, method);
      } else if (methodName.endsWith(ReadableBean.SUFFIX_PROPERTY)) {
        String propertyName = Character.toUpperCase(first)
            + methodName.substring(1, methodName.length() - ReadableBean.SUFFIX_PROPERTY.length());
        return new BeanOperationProperty(propertyName, method);
      } else {
        String propertyName = BeanHelper.getPropertyForGetter(methodName);
        if (propertyName != null) {
          return new BeanOperationGetter(propertyName, method);
        }
      }
    } else if (parameterCount == 1) {
      String propertyName = BeanHelper.getPropertyForSetter(methodName);
      if (propertyName != null) {
        return new BeanOperationSetter(propertyName, method);
      }
    }
    return null;
  }

  /**
   * @param method the default {@link Method}.
   * @return the {@link MethodHandle} for the given {@link Method}.
   */
  protected static MethodHandle createMethodHandle(Method method) {

    Class<?> declaringClass = method.getDeclaringClass();
    try {
      return MethodHandles.lookup().findSpecial(declaringClass, method.getName(),
          MethodType.methodType(method.getReturnType(), method.getParameterTypes()), declaringClass);
    } catch (Throwable e) {
      throw new IllegalStateException("Failed to create MethodHandle for " + method, e);
    }
  }

}
