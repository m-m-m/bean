package io.github.mmm.bean.factory.impl;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Interface for a simple wrapper of {@link #getRawClass()} and {@link #getGenericType()}.
 */
public class GenericTypeInfo {

  private final Class<?> rawClass;

  /** @see #getGenericType() */
  protected Type genericType;

  /**
   * The constructor.
   *
   * @param rawClass the {@link #getRawClass() raw class}.
   */
  protected GenericTypeInfo(Class<?> rawClass) {

    this(rawClass, null);
  }

  private GenericTypeInfo(Class<?> rawClass, Type genericType) {

    super();
    this.rawClass = rawClass;
    this.genericType = genericType;
  }

  /**
   * @return raw {@link Class}.
   */
  public Class<?> getRawClass() {

    return this.rawClass;
  }

  /**
   * @return the generic {@link Type}.
   */
  public Type getGenericType() {

    return this.genericType;
  }

  @Override
  public String toString() {

    return this.genericType.getTypeName();
  }

  /**
   * @param rawClass the {@link #getRawClass() raw class}.
   * @param genericType the {@link #getGenericType() generic type}.
   * @return the {@link GenericTypeInfo} for the given values.
   */
  public static GenericTypeInfo of(Class<?> rawClass, Type genericType) {

    if (genericType == null) {
      genericType = rawClass;
    }
    return new GenericTypeInfo(rawClass, genericType);
  }

  /**
   * @param method the {@link Method}.
   * @return the {@link GenericTypeInfo} for the {@link Method#getGenericReturnType() return type}.
   */
  public static GenericTypeInfo ofReturnType(Method method) {

    return new GenericTypeInfoReturnType(method);
  }

  /**
   * @param executable the {@link Executable}.
   * @param parameterIndex the index of the requested parameter.
   * @return the {@link GenericTypeInfo} for the {@link Executable#getGenericParameterTypes() parameter} at the
   *         specified {@code parameterIndex}.
   */
  public static GenericTypeInfo ofParameter(Executable executable, int parameterIndex) {

    return new GenericTypeInfoParameter(executable, parameterIndex);
  }

}
