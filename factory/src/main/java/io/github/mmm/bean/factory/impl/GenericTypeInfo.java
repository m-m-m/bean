package io.github.mmm.bean.factory.impl;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

/// Simple wrapper of [#getRawClass()] and [#getGenericType()].
public class GenericTypeInfo {

  private final Class<?> rawClass;

  /// @see #getGenericType()
  protected Type genericType;

  /// The constructor.
  ///
  /// @param rawClass the [raw class][#getRawClass()].
  protected GenericTypeInfo(Class<?> rawClass) {

    this(rawClass, null);
  }

  private GenericTypeInfo(Class<?> rawClass, Type genericType) {

    super();
    this.rawClass = rawClass;
    this.genericType = genericType;
  }

  /// @return raw [Class].
  public Class<?> getRawClass() {

    return this.rawClass;
  }

  /// @return the generic [Type].
  public Type getGenericType() {

    return this.genericType;
  }

  @Override
  public String toString() {

    return this.genericType.getTypeName();
  }

  /// @param rawClass the [raw class][#getRawClass()].
  /// @param genericType the [generic type][#getGenericType()].
  /// @return the [GenericTypeInfo] for the given values.
  public static GenericTypeInfo of(Class<?> rawClass, Type genericType) {

    if (genericType == null) {
      genericType = rawClass;
    }
    return new GenericTypeInfo(rawClass, genericType);
  }

  /// @param method the [Method].
  /// @return the [GenericTypeInfo] for the [return type][Method#getGenericReturnType()].
  public static GenericTypeInfo ofReturnType(Method method) {

    return new GenericTypeInfoReturnType(method);
  }

  /// @param executable the [Executable].
  /// @param parameterIndex the index of the requested parameter.
  /// @return the [GenericTypeInfo] for the [parameter][Executable#getGenericParameterTypes()] at the
  ///         specified `parameterIndex`.
  public static GenericTypeInfo ofParameter(Executable executable, int parameterIndex) {

    return new GenericTypeInfoParameter(executable, parameterIndex);
  }

  /// @param type the [generic type][#getGenericType()]. The [raw class][#getRawClass()] will be derived from
  ///        this [Type].
  /// @param containingType the [Type] containing the [Type] to resolve. E.g. the [Class] the
  ///        [Type] to resolve has been retrieved from from field, return type, parameter, type-parameter, etc.
  /// @return the [GenericTypeInfo] with the [raw class][GenericTypeInfo#getRawClass()] derived from the given
  ///         [Type]. Will be `null` if we can not properly resolve the raw class (e.g. TypeVariables are not
  ///         supported).
  public static GenericTypeInfo ofGeneric(Type type, Type containingType) {

    Class<?> rawClass = getRawClass(type, containingType);
    if (rawClass == null) {
      return null;
    }
    return of(rawClass, type);
  }

  /// @param type the generic [Type] to resolve as raw [Class].
  /// @param containingType the [Type] containing the [Type] to resolve. E.g. the [Class] the
  ///        [Type] to resolve has been retrieved from from field, return type, parameter, type-parameter, etc.
  /// @return the resolved raw [Class] of `null` if we can not properly resolve the raw class (e.g.
  ///         TypeVariable is not supported).
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
