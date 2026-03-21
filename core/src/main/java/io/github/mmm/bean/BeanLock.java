/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import io.github.mmm.property.PropertyLock;

/**
 * {@link PropertyLock} for {@link ReadableBean}. Usually the {@link ReadableBean} itself but for
 * {@link io.github.mmm.property.WritableProperty#getReadOnly() read-only properties}....
 *
 * @since 1.0.0
 */
public interface BeanLock extends PropertyLock {

  /**
   * @return the {@link BeanType} reflecting this {@link Bean}.
   * @see VirtualBean#getType()
   * @see BeanClass
   */
  BeanType getType();

}
