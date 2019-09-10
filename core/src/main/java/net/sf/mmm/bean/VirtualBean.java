/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

/**
 * {@link WritableBean} that is {@link BeanType#isVirtual() virtual}.
 */
public interface VirtualBean extends WritableBean {

  @Override
  BeanClass getType();

  @Override
  default String getPropertyNameForAlias(String alias) {

    return getType().getPropertyNameForAlias(alias);
  }

}
