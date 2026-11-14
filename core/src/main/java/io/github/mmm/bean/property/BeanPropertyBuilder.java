/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.property;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.PropertyLock;
import io.github.mmm.property.PropertyMetadata;
import io.github.mmm.property.builder.PropertyBuilder;
import io.github.mmm.validation.main.ValidatorBuilderObject;

/// [PropertyBuilder] for [BeanProperty].
///
/// @param <V> type of the [WritableBean].
/// @since 1.0.0
public class BeanPropertyBuilder<V extends WritableBean> extends
    PropertyBuilder<V, BeanProperty<V>, ValidatorBuilderObject<V, BeanPropertyBuilder<V>>, BeanPropertyBuilder<V>> {

  private Class<V> valueClass;

  /// The constructor.
  ///
  /// @param lock the [lock][#getLock()].
  public BeanPropertyBuilder(PropertyLock lock) {

    this(lock, null);
  }

  /// The constructor.
  ///
  /// @param lock the [lock][#getLock()].
  /// @param valueClass the [value class][#valueClass(Class)].
  public BeanPropertyBuilder(PropertyLock lock, Class<V> valueClass) {

    super(lock);
    this.valueClass = valueClass;
  }

  /// @param valueType the [value class][BeanProperty#getValueClass()].
  /// @return this builder itself (`this`) for fluent API calls.
  public BeanPropertyBuilder<V> valueClass(Class<V> valueType) {

    this.valueClass = valueType;
    return this;
  }

  @Override
  protected ValidatorBuilderObject<V, BeanPropertyBuilder<V>> createValidatorBuilder() {

    return new ValidatorBuilderObject<>(this);
  }

  @Override
  protected BeanProperty<V> build(String name, PropertyMetadata<V> metadata) {

    return new BeanProperty<>(name, this.valueClass, metadata);
  }

}
