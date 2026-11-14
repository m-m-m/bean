/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

/// A [Bean] that is always [dynamic][#isDynamic()]. Only exists for simplicity.
@BeanName("mmm_DynamicBean")
public class DynamicBean extends Bean {

  /// The constructor.
  public DynamicBean() {

    this(null);
  }

  /// The constructor.
  ///
  /// @param writable the [WritableBean] to wrap as [read-only][#isReadOnly()] bean or `null` to create
  ///        a mutable bean.
  public DynamicBean(WritableBean writable) {

    super(writable);
  }

  @Override
  public boolean isDynamic() {

    return true;
  }

}
