package io.github.mmm.bean.impl.alias;

import io.github.mmm.bean.BeanAliasMap;

/// Abstract base implementation of [BeanAliasMap].
public abstract class AbstractBeanAliasMap implements BeanAliasMap {

  /// @param name the primary [property name][io.github.mmm.property.ReadableProperty#getName()].
  /// @param alias the [alias mapping][#getName(String)] for the given `name`.
  /// @return the [AbstractBeanAliasMap] with the given mapping added.
  public abstract AbstractBeanAliasMap add(String name, String alias);

}
