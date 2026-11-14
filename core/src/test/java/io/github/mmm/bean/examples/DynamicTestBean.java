/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.examples;

import io.github.mmm.bean.WritableBean;

/// [TestBean] that is [dynamic][#isDynamic()].
public class DynamicTestBean extends TestBean {

  /// The constructor.
  public DynamicTestBean() {

    this(null);
  }

  /// The constructor.
  ///
  /// @param writable the [WritableBean] to wrap as [read-only][#isReadOnly()] bean or `null` to create
  ///        a mutable bean.
  public DynamicTestBean(WritableBean writable) {

    super(writable);
  }

  @Override
  public boolean isDynamic() {

    return true;
  }

}
