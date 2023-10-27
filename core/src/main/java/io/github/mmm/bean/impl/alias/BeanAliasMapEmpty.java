package io.github.mmm.bean.impl.alias;

import java.util.Collections;
import java.util.List;

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
  public List<String> getAliases(String name) {

    return Collections.emptyList();
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
