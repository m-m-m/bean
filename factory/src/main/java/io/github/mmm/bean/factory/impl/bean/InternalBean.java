package io.github.mmm.bean.factory.impl.bean;

import io.github.mmm.bean.BeanAliasMap;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.ReadableProperty;

/// Interface to register aliases for a simple bean.
public interface InternalBean extends WritableBean {

  /// @param name the [property name][ReadableProperty#getName()].
  /// @param aliases the [aliases][BeanAliasMap#getAliases(String)] to add.
  /// @see io.github.mmm.bean.ReadableBean#getAliases()
  void registerAliases(String name, String... aliases);

  /// @param polymorphic new value of [#isPolymorphic()].
  void setPolymorphic(boolean polymorphic);

}
