/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

/**
 * {@link WritableBean} that is {@link BeanType#isVirtual() virtual}.
 */
public interface VirtualBean extends WritableBean {

  @Override
  BeanClass getType();

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
