package io.github.mmm.bean.mapping;

import java.util.Iterator;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.bean.impl.mapping.ClassNameMapperProvider;

/**
 * Interface for a simple component capable of {@link #getName(Class) getting the name} for a {@link Class} and
 * {@link #getClass(String) vice-versa}. The implementation is designed in a secure way as described in
 * {@link #getClass(String)}.<br>
 * This interface is designed for frameworks that need to do heavy generic stuff. It should not be used by end-users and
 * the {@link #get() lazy initialization} may consume some time.
 *
 * @since 1.0.0
 * @see #get()
 */
public abstract interface ClassNameMapper {

  /**
   * @param javaClass the {@link Class} reflecting the type (class, interface, etc.) to map.
   * @return the name of the given {@link Class}. By default this is the {@link Class#getName() qualified name} but it
   *         may also be the {@link Class#getSimpleName() simple name} or something else as long as it is unique.
   * @throws ObjectNotFoundException if the given {@link Class} is not mapped.
   */
  String getName(Class<?> javaClass);

  /**
   * @param javaClass the {@link Class} reflecting the type (class, interface, etc.) to map.
   * @return the name of the given {@link Class}. By default this is the {@link Class#getName() qualified name} but it
   *         may also be the {@link Class#getSimpleName() simple name} or something else as long as it is unique. If the
   *         given {@link Class} is not {@link #contains(Class) mapped}, the {@link Class#getName() qualified name} of
   *         the given {@link Class} is returned.
   */
  String getNameOrQualified(Class<?> javaClass);

  /**
   * @param name the {@link #getName(Class) name} of the {@link Class}.
   * @return the {@link Class} reflecting the type with the given {@code name}. Will be {@code null} if the type is not
   *         mapped. For security reasons by design no reflective lookup (class-loading) is triggered. This allows to
   *         deserialize or unmarshall polymorphic or generic data without allowing external users (or attackers) to
   *         trigger class-loading and potential code execution.
   * @throws ObjectNotFoundException if the given {@code name} is not mapped.
   */
  Class<?> getClass(String name);

  /**
   * @param javaClass the {@link #getClass(String) class} to check.
   * @return {@code true} if the given {@link #getClass(String) class} is mapped by this {@link ClassNameMapper},
   *         {@code false} otherwise.
   */
  boolean contains(Class<?> javaClass);

  /**
   * @param name the {@link #getName(Class) name} to check.
   * @return {@code true} if the given {@link #getName(Class) name} is mapped by this {@link ClassNameMapper},
   *         {@code false} otherwise.
   */
  boolean contains(String name);

  /**
   * @param classType the {@link ClassType} for which the {@link Class}es are requested.
   * @return an {@link Iterator} of all {@link Class}es registered for the given {@link ClassType}.
   */
  Iterator<Class<?>> getClasses(ClassType classType);

  /**
   * When you need to use this method, you need to have the following "requires [transitive]" statement in your module.
   *
   * <pre>
   * requires io.github.mmm.bean.factory;
   * </pre>
   *
   * @return the singleton instance of {@link ClassNameMapper}.
   */
  public static ClassNameMapper get() {

    return ClassNameMapperProvider.MAPPER;
  }

  /**
   * Type of {@link Class}es managed by this {@link ClassNameMapper}.
   */
  public static enum ClassType {

    /**
     * A datatype is an immutable value type such as {@link String}, {@link Boolean}, {@link Long},
     * {@link java.time.LocalDateTime}, etc.
     */
    DATATYPE,

    /**
     * A bean is a (non-abstract) class or interface derived from {@link io.github.mmm.bean.WritableBean}.
     */
    BEAN
  }
}
