package io.github.mmm.bean.factory.impl;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Implementation of {@link GenericTypeInfo} for {@link Method} {@link Method#getReturnType() return type}.
 */
public class GenericTypeInfoReturnType extends GenericTypeInfo {

  private final Method method;

  /**
   * The constructor.
   *
   * @param method the {@link Method}.
   */
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
