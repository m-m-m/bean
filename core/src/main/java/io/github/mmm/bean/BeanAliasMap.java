package io.github.mmm.bean;

import java.util.Collection;

import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;

/**
 * An alias is an alternative for a {@link ReadableBean#getProperty(String) property} {@link ReadableProperty#getName()
 * name}. It allows to {@link ReadableBean#getProperty(String) retrieve a property} not only via its primary
 * {@link ReadableProperty#getName() name} but also under one or multiple aliases. An alias could e.g. be a legacy name
 * after a {@link ReadableProperty property} has been renamed or to use a technical name containing special characters
 * (e.g. "@" or ".") for very specific cases.
 */
public interface BeanAliasMap {

  /**
   * @param alias the alias of a {@link WritableProperty#getName() property name}.
   * @return the primary {@link WritableProperty#getName() property name} for the given {@code alias} or {@code null} if
   *         no such alias is defined.
   * @see #getAliases(String)
   */
  String getName(String alias);

  /**
   * Inverse operation of {@link #getName(String)}.
   *
   * @param name the primary {@link WritableProperty#getName() property name}.
   * @return a {@link Collection} with all registered {@link #getName(String) aliases} for the given {@code name}. Will
   *         be empty if no such {@link #getName(String) alias} is defined.
   * @see #getName(String)
   */
  Collection<String> getAliases(String name);

}
