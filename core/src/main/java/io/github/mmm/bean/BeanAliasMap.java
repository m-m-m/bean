package io.github.mmm.bean;

import java.util.List;

import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;

/// An alias is an alternative for a [property][ReadableBean#getProperty(String)] [name][ReadableProperty#getName()]. It allows to [retrieve a property][ReadableBean#getProperty(String)] not only via its primary
/// [name][ReadableProperty#getName()] but also under one or multiple aliases. An alias could e.g. be a legacy name
/// after a [property][ReadableProperty] has been renamed or to use a technical name containing special characters
/// (e.g. "@" or ".") for very specific cases.
public interface BeanAliasMap {

  /// @param alias the alias of a [property name][WritableProperty#getName()].
  /// @return the primary [property name][WritableProperty#getName()] for the given `alias` or `null` if
  ///         no such alias is defined.
  /// @see #getAliases(String)
  String getName(String alias);

  /// Inverse operation of [#getName(String)].
  ///
  /// @param name the primary [property name][WritableProperty#getName()].
  /// @return a [List] with all registered [aliases][#getName(String)] for the given `name`. Will be
  ///         empty if no such [alias][#getName(String)] is defined.
  /// @see #getName(String)
  List<String> getAliases(String name);

}
