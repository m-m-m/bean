/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.bean;

import io.github.mmm.bean.AbstractVirtualBean;
import io.github.mmm.bean.BeanClass;
import io.github.mmm.bean.BeanLock;
import io.github.mmm.bean.VirtualBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.property.PropertyLock;

/**
 * Internal implementation of {@link VirtualBean} used from {@link BeanProxy}.
 */
public final class SimpleVirtualBean extends AbstractVirtualBean implements InternalBean {

  private final WritableBean proxy;

  private boolean polymorphic;

  /**
   * The constructor.
   *
   * @param writable the {@link WritableBean} to wrap as {@link #isReadOnly() read-only} bean or {@code null} to create
   *        a mutable bean.
   */
  public SimpleVirtualBean(WritableBean writable) {

    this(writable, null, ((SimpleVirtualBean) writable).proxy);
  }

  /**
   * The constructor.
   *
   * @param writable the {@link WritableBean} to wrap as {@link #isReadOnly() read-only} bean or {@code null} to create
   *        a mutable bean.
   * @param type the {@link #getType() type}.
   * @param proxy the {@link WritableBean} instance which is the outer dynamic proxy.
   */
  public SimpleVirtualBean(WritableBean writable, BeanClass type, WritableBean proxy) {

    super(writable, type);
    this.proxy = proxy;
  }

  @Override
  public boolean isPolymorphic() {

    return this.polymorphic;
  }

  @Override
  public void setPolymorphic(boolean polymorphic) {

    this.polymorphic = polymorphic;
  }

  @Override
  protected boolean isLockOwnerInternal(PropertyLock lock) {

    return (lock == this.proxy);
  }

  @Override
  protected BeanLock getReadOnlyOuterLock() {

    return this.proxy.getReadOnly();
  }

  @Override
  public void registerAliases(String propertyName, String... propertyAliases) {

    super.registerAliases(propertyName, propertyAliases);
  }

}
