/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import io.github.mmm.bean.impl.BeanTypeImpl;

/// Regular implementation of [WritableBean]. For [VirtualBean]s use [AdvancedBean] instead.
public class Bean extends AbstractBean {

  private final BeanType type;

  /// The constructor.
  public Bean() {

    this(null);
  }

  /// The constructor.
  ///
  /// @param writable the [WritableBean] to wrap as [read-only][#isReadOnly()] bean or `null` to create
  ///        a mutable bean.
  public Bean(WritableBean writable) {

    super(writable);
    this.type = BeanTypeImpl.asType(getClass());
  }

  @Override
  public BeanType getType() {

    return this.type;
  }

  @Override
  public final boolean isPrototype() {

    return false;
  }

}
