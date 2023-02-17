package io.github.mmm.bean.impl;

import java.util.Collection;
import java.util.Collections;

import io.github.mmm.bean.BeanAliasMap;

/**
 * Implementation of {@link BeanAliasMap} that is empty.
 */
public class BeanAliasMapEmpty extends AbstractBeanAliasMap {

  /** The singleton instance. */
  public static final AbstractBeanAliasMap INSTANCE = new BeanAliasMapEmpty();

  private BeanAliasMapEmpty() {

    super();
  }

  @Override
  public String getName(String alias) {

    return null;
  }

  @Override
  public Collection<String> getAliases(String name) {

    return Collections.emptySet();
  }

  @Override
  public AbstractBeanAliasMap add(String name, String alias) {

    return new BeanAliasMapSingle(name, alias);
  }

  @Override
  public String toString() {

    return "Aliases: {}";
  }

}
