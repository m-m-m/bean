package io.github.mmm.bean.factory.impl;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Implementation of {@link GenericTypeInfo} for {@link Method} {@link Method#getReturnType() return type}.
 */
public class GenericTypeInfoParameter extends GenericTypeInfo {

  private final Executable executable;

  private final int parameter;

  /**
   * The constructor.
   *
   * @param executable the {@link Executable}.
   * @param parameter the {@link Executable#getParameters() parameter} index.
   */
  public GenericTypeInfoParameter(Executable executable, int parameter) {

    super(executable.getParameterTypes()[parameter]);
    this.executable = executable;
    this.parameter = parameter;
  }

  @Override
  public Type getGenericType() {

    if (this.genericType == null) {
      this.genericType = this.executable.getGenericParameterTypes()[this.parameter];
    }
    return this.genericType;
  }

}
