/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.bean;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.BeanLock;
import io.github.mmm.bean.BeanType;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.property.PropertyLock;

/// Internal implementation of [WritableBean] non virtual instance used from [BeanProxy].
///
/// @since 1.0.0
public final class SimpleBean extends AbstractBean implements InternalBean {

  private final BeanType type;

  private final WritableBean proxy;

  private boolean polymorphic;

  /// The constructor.
  ///
  /// @param writable the [WritableBean] to wrap as [read-only][#isReadOnly()] bean or `null` to create
  ///        a mutable bean.
  public SimpleBean(WritableBean writable) {

    this(writable, null, ((SimpleBean) writable).proxy);
  }

  /// The constructor.
  ///
  /// @param writable the [WritableBean] to wrap as [read-only][#isReadOnly()] bean or `null` to create
  ///        a mutable bean.
  /// @param type the [type][#getType()].
  /// @param proxy the [WritableBean] instance which is the outer dynamic proxy.
  public SimpleBean(WritableBean writable, BeanType type, WritableBean proxy) {

    super(writable);
    if ((type == null) && (writable != null)) {
      type = writable.getType();
    }
    this.type = type;
    this.proxy = proxy;
  }

  @Override
  public BeanType getType() {

    return this.type;
  }

  @Override
  public boolean isPrototype() {

    return false;
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
  public boolean isDynamic() {

    return true;
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
