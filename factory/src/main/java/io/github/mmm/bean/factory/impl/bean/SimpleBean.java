/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.bean;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.BeanType;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.property.AttributeReadOnly;

/**
 * Internal implementation of {@link WritableBean} non virtual instance used from {@link BeanProxy}.
 *
 * @since 1.0.0
 */
public final class SimpleBean extends AbstractBean implements SimpleBeanAliasAccess {

  private final BeanType type;

  /**
   * The constructor.
   *
   * @param writable the {@link WritableBean} to wrap as {@link #isReadOnly() read-only} bean or {@code null} to create
   *        a mutable bean.
   */
  public SimpleBean(WritableBean writable) {

    this(writable, null);
  }

  /**
   * The constructor.
   *
   * @param writable the {@link WritableBean} to wrap as {@link #isReadOnly() read-only} bean or {@code null} to create
   *        a mutable bean.
   * @param type the {@link #getType() type}.
   */
  public SimpleBean(WritableBean writable, BeanType type) {

    super(writable);
    if ((type == null) && (writable != null)) {
      type = writable.getType();
    }
    this.type = type;
  }

  @Override
  public BeanType getType() {

    return this.type;
  }

  @Override
  public final boolean isPrototype() {

    return false;
  }

  @Override
  public boolean isDynamic() {

    return true;
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
