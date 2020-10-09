/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl;

import io.github.mmm.bean.AbstractVirtualBean;
import io.github.mmm.bean.BeanClass;
import io.github.mmm.bean.VirtualBean;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;

/**
 * Internal implementation of {@link VirtualBean} used from {@link BeanProxy}.
 */
public final class SimpleVirtualBean extends AbstractVirtualBean {

  /**
   * The constructor.
   */
  public SimpleVirtualBean() {

    super();
  }

  /**
   * The constructor.
   *
   * @param type the {@link #getType() type}.
   */
  public SimpleVirtualBean(BeanClass type) {

    super(type);
  }

}
