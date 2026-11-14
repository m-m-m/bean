/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.operation;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import io.github.mmm.bean.BeanHelper;
import io.github.mmm.bean.factory.impl.bean.InternalBean;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.bean.factory.impl.proxy.BeanProxyPrototype;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.factory.PropertyFactory;

/// Operation on a [BeanProxy].
///
/// @since 1.0.0
public abstract class BeanOperation {

  /// Empty [Object] array.
  protected static final Object[] NO_ARGS = new Object[0];

  /// The constructor.
  public BeanOperation() {

    super();
  }

  /// Invokes this operation of the given `access` using the given arguments.
  ///
  /// @param proxy the [BeanProxy].
  /// @param args the arguments of the reflective method invocation.
  /// @return the result of the invocation.
  /// @throws Throwable if something goes wrong.
  public abstract Object invoke(BeanProxy proxy, Object[] args) throws Throwable;

  /// Invokes the given [default][Method#isDefault()] [Method].
  ///
  /// @param proxy the [BeanProxy] to operate on.
  /// @param method the [default][Method#isDefault()] [Method].
  /// @param args the method parameters.
  /// @return the result of the method invocation.
  /// @throws Throwable if something went wrong.
  protected Object invokeDefaultMethod(BeanProxy proxy, Method method, Object[] args) throws Throwable {

    try {
      return InvocationHandler.invokeDefault(proxy.getProxy(), method, args);
    } catch (NoSuchMethodError e) {
      throw new IllegalStateException(e);
    }
  }

  /// @return `true` if the [WritableProperty] is required for this operation, `false` otherwise.
  public boolean isPropertyRequired() {

    return false;
  }

  /// @return the [property name][io.github.mmm.property.Property#getName()]. May be `null` if not a property
  ///         related method.
  public String getPropertyName() {

    return null;
  }

  /// @param proxy the [BeanProxy].
  /// @return a new [WritableProperty] for this operation. May be `null` if not a property related method.
  public WritableProperty<?> createProperty(BeanProxy proxy) {

    return null;
  }

  /// @param bean the [InternalBean] where to register potential aliases.
  public void registerAliases(InternalBean bean) {

  }

  /// @param method the [Method].
  /// @param prototype the [BeanProxyPrototype].
  /// @return the according new [BeanOperation].
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
