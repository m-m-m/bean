package io.github.mmm.bean.factory.impl;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/// Implementation of [GenericTypeInfo] for [Method] [return type][Method#getReturnType()].
public class GenericTypeInfoParameter extends GenericTypeInfo {

  private final Executable executable;

  private final int parameter;

  /// The constructor.
  ///
  /// @param executable the [Executable].
  /// @param parameter the [parameter][Executable#getParameters()] index.
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
