package io.github.mmm.bean.impl;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import io.github.mmm.bean.BeanAliasMap;

/**
 * Implementation of {@link BeanAliasMap} that has a single mapping.
 */
public class BeanAliasMapSingle extends AbstractBeanAliasMap {

  private final String name;

  private final String alias;

  private final Set<String> aliases;

  /**
   * The constructor.
   *
   * @param name the primary {@link io.github.mmm.property.ReadableProperty#getName() property name}.
   * @param alias the associated {@link #getName(String) alias}.
   */
  public BeanAliasMapSingle(String name, String alias) {

    super();
    this.name = name;
    this.alias = alias;
    this.aliases = Collections.singleton(this.alias);
  }

  @Override
  public String getName(String propertyAlias) {

    if (this.alias.equals(propertyAlias)) {
      return this.name;
    }
    return null;
  }

  @Override
  public Collection<String> getAliases(String propertyName) {

    if (this.name.equals(propertyName)) {
      return this.aliases;
    }
    return Collections.emptySet();
  }

  @Override
  public AbstractBeanAliasMap add(String propertyName, String propertyAlias) {

    BeanAliasMapMultiple map = new BeanAliasMapMultiple(this.name, this.alias);
    return map.add(propertyName, propertyAlias);
  }

  @Override
  public String toString() {

    return "Aliases: {" + this.name + "=[" + this.alias + "]}";
  }

}
