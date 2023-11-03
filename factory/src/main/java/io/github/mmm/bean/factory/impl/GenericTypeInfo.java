package io.github.mmm.bean.factory.impl;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/**
 * Simple wrapper of {@link #getRawClass()} and {@link #getGenericType()}.
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

  /**
   * @param type the {@link #getGenericType() generic type}. The {@link #getRawClass() raw class} will be derived from
   *        this {@link Type}.
   * @param containingType the {@link Type} containing the {@link Type} to resolve. E.g. the {@link Class} the
   *        {@link Type} to resolve has been retrieved from from field, return type, parameter, type-parameter, etc.
   * @return the {@link GenericTypeInfo} with the {@link GenericTypeInfo#getRawClass() raw class} derived from the given
   *         {@link Type}. Will be {@code null} if we can not properly resolve the raw class (e.g. TypeVariables are not
   *         supported).
   */
  public static GenericTypeInfo ofGeneric(Type type, Type containingType) {

    Class<?> rawClass = getRawClass(type, containingType);
    if (rawClass == null) {
      return null;
    }
    return of(rawClass, type);
  }

  /**
   * @param type the generic {@link Type} to resolve as raw {@link Class}.
   * @param containingType the {@link Type} containing the {@link Type} to resolve. E.g. the {@link Class} the
   *        {@link Type} to resolve has been retrieved from from field, return type, parameter, type-parameter, etc.
   * @return the resolved raw {@link Class} of {@code null} if we can not properly resolve the raw class (e.g.
   *         TypeVariable is not supported).
   */
  public static Class<?> getRawClass(Type type, Type containingType) {

    return getRawClass(type, containingType, type);
  }

  // We are fully aware that this in not a correct solution for the problem.
  // However, the JDK does not offer an API to solve this problem and we already solved it earlier but with a very
  // high complexity. As we actually want to go away from deep reflection, we avoid the complexity here.
  // If you have a bean using a generic returning ListProperty<T> this will simply not be able to resolve the real class
  // for T
  private static Class<?> getRawClass(Type type, Type containingType, Type root) {

    if (type instanceof Class<?> clazz) {
      return clazz;
    } else if (type instanceof ParameterizedType pType) {
      return getRawClass(pType.getRawType(), containingType, root);
    } else if (type instanceof WildcardType wType) {
      Type[] bounds = wType.getUpperBounds();
      if (bounds.length > 0) {
        return getRawClass(bounds[0], containingType, root);
      }
    }
    return null;
  }
}
