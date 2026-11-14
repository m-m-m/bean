package io.github.mmm.bean.mapping;

import java.util.Iterator;

import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.bean.impl.mapping.ClassNameMapperProvider;

/// Interface for a simple component capable of [getting the name][#getName(Class)] for a [Class] and
/// [vice-versa][#getClass(String)]. The implementation is designed in a secure way as described in
/// [#getClass(String)]. This interface is designed for frameworks that need to do heavy generic stuff. It should not be
/// used by end-users and the [lazy initialization][#get()] may consume some time.
///
/// @since 1.0.0
/// @see #get()
public abstract interface ClassNameMapper {

  /// @param javaClass the [Class] reflecting the type (class, interface, etc.) to map.
  /// @return the name of the given [Class]. By default this is the [qualified name][Class#getName()] but it may also be
  ///         the [simple name][Class#getSimpleName()] or something else as long as it is unique.
  /// @throws ObjectNotFoundException if the given [Class] is not mapped.
  String getName(Class<?> javaClass);

  /// @param javaClass the [Class] reflecting the type (class, interface, etc.) to map.
  /// @return the name of the given [Class]. By default this is the [qualified name][Class#getName()] but it may also be
  ///         the [simple name][Class#getSimpleName()] or something else as long as it is unique. If the given [Class] is
  ///         not [mapped][#contains(Class)], the [qualified name][Class#getName()] of the given [Class] is returned.
  String getNameOrQualified(Class<?> javaClass);

  /// @param name the [name][#getName(Class)] of the [Class].
  /// @return the [Class] reflecting the type with the given `name`. Will be `null` if the type is not mapped. For
  ///         security reasons by design no reflective lookup (class-loading) is triggered. This allows to deserialize or
  ///         unmarshall polymorphic or generic data without allowing external users (or attackers) to trigger
  ///         class-loading and potential code execution.
  /// @throws ObjectNotFoundException if the given `name` is not mapped.
  Class<?> getClass(String name);

  /// @param javaClass the [class][#getClass(String)] to check.
  /// @return `true` if the given [class][#getClass(String)] is mapped by this [ClassNameMapper], `false` otherwise.
  boolean contains(Class<?> javaClass);

  /// @param name the [name][#getName(Class)] to check.
  /// @return `true` if the given [name][#getName(Class)] is mapped by this [ClassNameMapper], `false` otherwise.
  boolean contains(String name);

  /// @param classType the [ClassType] for which the [Class]es are requested.
  /// @return an [Iterator] of all [Class]es registered for the given [ClassType].
  Iterator<Class<?>> getClasses(ClassType classType);

  /// When you need to use this method, you need to have the following "requires \[transitive\]" statement in your
  /// module.
  ///
  /// ``` requires io.github.mmm.bean.factory; ```
  ///
  /// @return the singleton instance of [ClassNameMapper].
  public static ClassNameMapper get() {

    return ClassNameMapperProvider.MAPPER;
  }

  /// Type of [Class]es managed by this [ClassNameMapper].
  public static enum ClassType {

    /// A datatype is an immutable value type such as [String], [Boolean], [Long], [java.time.LocalDateTime], etc.
    DATATYPE,

    /// A bean is a (non-abstract) class or interface derived from [io.github.mmm.bean.WritableBean].
    BEAN
  }
}
