/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.impl;

import io.github.mmm.property.PropertyMetadata;

/**
 * {@link PropertyMetadata} for a {@link io.github.mmm.bean.AbstractBean}.
 *
 * @param <V> type of the {@link io.github.mmm.property.Property#get() property value}.
 * @since 1.0.0
 */
public interface BeanPropertyMetadata<V> extends PropertyMetadata<V> {

  /**
   * @param value the fixed value to set as {@link #getExpression() expression} to make the property read-only.
   */
  void makeReadOnly(V value);

}
