/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

/// [WritableBean] that may be [virtual][BeanType#isVirtual()] so it can potentially represent types that do
/// not exist as Java [Class]. Further, it has a [prototype][BeanClass#getPrototype()] that is typically
/// [dynamic][#isDynamic()]. If a [io.github.mmm.property.Property] is added to the prototype, it will
/// automatically be available by all instances and [subclasses][BeanClass#isSubclassOf(BeanClass)].
@AbstractInterface
public interface VirtualBean extends WritableBean {

  @Override
  BeanClass getType();

  /// @param beanClass the [BeanClass] to check.
  /// @return `true` if this [VirtualBean] is an instance of the given [BeanClass], `false`
  ///         otherwise.
  default boolean isInstanceOf(BeanClass beanClass) {

    if (beanClass == null) {
      return false;
    }
    return getType().isSubclassOf(beanClass, true, true);
  }

  /// @param <B> type of the [VirtualBean].
  /// @param bean the [VirtualBean] to get the prototype of.
  /// @return the [prototype][BeanClass#getPrototype()] of the given [VirtualBean].
  @SuppressWarnings("unchecked")
  static <B extends VirtualBean> B getPrototype(B bean) {

    return (B) bean.getType().getPrototype();
  }

}
