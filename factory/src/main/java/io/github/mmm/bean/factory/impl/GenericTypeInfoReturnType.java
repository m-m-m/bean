package io.github.mmm.bean.factory.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/// Implementation of [GenericTypeInfo] for [Method] [return type][Method#getReturnType()].
public class GenericTypeInfoReturnType extends GenericTypeInfo {

  private final Method method;

  /// The constructor.
  ///
  /// @param method the [Method].
  public GenericTypeInfoReturnType(Method method) {

    super(method.getReturnType());
    this.method = method;
  }

  @Override
  public Type getGenericType() {

    if (this.genericType == null) {
      this.genericType = this.method.getGenericReturnType();
    }
    return this.genericType;
  }

}
