package io.github.mmm.bean.impl.alias;

import io.github.mmm.bean.BeanAliasMap;

/**
 * Abstract base implementation of {@link BeanAliasMap}.
 */
public abstract class AbstractBeanAliasMap implements BeanAliasMap {

  /**
   * @param name the primary {@link io.github.mmm.property.ReadableProperty#getName() property name}.
   * @param alias the {@link #getName(String) alias mapping} for the given {@code name}.
   * @return the {@link AbstractBeanAliasMap} with the given mapping added.
   */
  public abstract AbstractBeanAliasMap add(String name, String alias);

}
