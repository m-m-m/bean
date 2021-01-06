/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

/**
 * {@link WritableBean} that may be {@link BeanType#isVirtual() virtual} so it can potentially represent types that do
 * not exist as Java {@link Class}. Further, it has a {@link BeanClass#getPrototype() prototype} that is typically
 * {@link #isDynamic() dynamic}. If a {@link io.github.mmm.property.Property} is added to the prototype, it will
 * automatically be available by all instances and {@link BeanClass#isSubclassOf(BeanClass) subclasses}.
 */
@AbstractInterface
public interface VirtualBean extends WritableBean {

  @Override
  BeanClass getType();

  /**
   * @param beanClass the {@link BeanClass} to check.
   * @return {@code true} if this {@link VirtualBean} is an instance of the given {@link BeanClass}, {@code false}
   *         otherwise.
   */
  default boolean isInstanceOf(BeanClass beanClass) {

    if (beanClass == null) {
      return false;
    }
    return getType().isSubclassOf(beanClass, true, true);
  }

  /**
   * @param <B> type of the {@link VirtualBean}.
   * @param bean the {@link VirtualBean} to get the prototype of.
   * @return the {@link BeanClass#getPrototype() prototype} of the given {@link VirtualBean}.
   */
  @SuppressWarnings("unchecked")
  static <B extends VirtualBean> B getPrototype(B bean) {

    return (B) bean.getType().getPrototype();
  }

}
