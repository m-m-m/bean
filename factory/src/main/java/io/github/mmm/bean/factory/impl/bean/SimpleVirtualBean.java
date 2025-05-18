/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.bean;

import io.github.mmm.bean.AbstractVirtualBean;
import io.github.mmm.bean.BeanClass;
import io.github.mmm.bean.VirtualBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.property.AttributeReadOnly;

/**
 * Internal implementation of {@link VirtualBean} used from {@link BeanProxy}.
 */
public final class SimpleVirtualBean extends AbstractVirtualBean implements SimpleBeanAliasAccess {

  /**
   * The constructor.
   */
  public SimpleVirtualBean() {

    super();
  }

  /**
   * The constructor.
   *
   * @param writable the {@link WritableBean} to wrap as {@link #isReadOnly() read-only} bean or {@code null} to create
   *        a mutable bean.
   * @param type the {@link #getType() type}.
   */
  public SimpleVirtualBean(WritableBean writable, BeanClass type) {

    super(writable, type);
  }

  @Override
  protected boolean isLockOwnerInternal(AttributeReadOnly lock) {

    // check more accurate?
    return true;
  }

  @Override
  public void registerAliases(String propertyName, String... propertyAliases) {

    super.registerAliases(propertyName, propertyAliases);
  }

}
