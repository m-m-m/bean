package io.github.mmm.bean.factory.impl.bean;

import io.github.mmm.bean.BeanAliasMap;
import io.github.mmm.property.ReadableProperty;

/**
 * Interface to register aliases for a simple bean.
 */
public interface SimpleBeanAliasAccess {

  /**
   * @param name the {@link ReadableProperty#getName() property name}.
   * @param aliases the {@link BeanAliasMap#getAliases(String) aliases} to add.
   * @see io.github.mmm.bean.ReadableBean#getAliases()
   */
  void registerAliases(String name, String... aliases);

}
