/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import io.github.mmm.property.PropertyLock;

/// [PropertyLock] for [ReadableBean]. Usually the [ReadableBean] itself but for
/// [read-only properties][io.github.mmm.property.WritableProperty#getReadOnly()]....
///
/// @since 1.0.0
public interface BeanLock extends PropertyLock {

  /// @return the [BeanType] reflecting this [Bean].
  /// @see VirtualBean#getType()
  /// @see BeanClass
  BeanType getType();

}
