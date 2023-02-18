package io.github.mmm.bean.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.bean.BeanAliasMap;

/**
 * Implementation of {@link BeanAliasMap} that has a multiple mappings.
 */
public class BeanAliasMapMultiple extends AbstractBeanAliasMap {

  private static final Logger LOG = LoggerFactory.getLogger(BeanAliasMapMultiple.class);

  private final Map<String, String> alias2nameMap;

  private final Map<String, List<String>> aliasesMap;

  /**
   * The constructor.
   *
   * @param name the primary {@link io.github.mmm.property.ReadableProperty#getName() property name}.
   * @param alias the associated {@link #getName(String) alias}.
   */
  public BeanAliasMapMultiple(String name, String alias) {

    super();
    this.alias2nameMap = new HashMap<>();
    this.aliasesMap = new HashMap<>();
    register(name, alias);
  }

  private void register(String name, String alias) {

    String duplicate = this.alias2nameMap.put(alias, name);
    if (duplicate != null) {
      LOG.error("Duplicate alias '{}' for name '{}' and '{}'.", alias, duplicate, name);
      if (name.equals(duplicate)) {
        return;
      }
    }
    List<String> aliases = this.aliasesMap.computeIfAbsent(name, x -> new ArrayList<>());
    aliases.add(alias);
  }

  @Override
  public String getName(String alias) {

    return this.alias2nameMap.get(alias);
  }

  @Override
  public List<String> getAliases(String name) {

    List<String> aliases = this.aliasesMap.get(name);
    if (aliases == null) {
      return Collections.emptyList();
    } else {
      return Collections.unmodifiableList(aliases);
    }
  }

  @Override
  public AbstractBeanAliasMap add(String name, String alias) {

    register(name, alias);
    return this;
  }

  @Override
  public String toString() {

    return "Aliases: " + this.aliasesMap;
  }

}
