/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.property;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.Property;
import io.github.mmm.property.PropertyMetadata;

/**
 * Implementation of {@link BeanProperty}.
 *
 * @param <V> type of the {@link WritableBean bean} {@link #getValue() value}.
 * @since 1.0.0
 */
public class BeanProperty<V extends WritableBean> extends Property<V> implements WritableBeanProperty<V> {

  private final Class<V> valueClass;

  private V value;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param valueClass the {@link #getValueClass() value class}.
   */
  public BeanProperty(String name, Class<V> valueClass) {

    this(name, valueClass, null);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param valueClass the {@link #getValueClass() value class}.
   * @param metadata the {@link #getMetadata() metadata}.
   */
  public BeanProperty(String name, Class<V> valueClass, PropertyMetadata<V> metadata) {

    super(name, metadata);
    this.valueClass = valueClass;
  }

  @Override
  protected V doGetValue() {

    return this.value;
  }

  @Override
  protected void doSetValue(V newValue) {

    this.value = newValue;
  }

  @Override
  public Class<V> getValueClass() {

    return this.valueClass;
  }

  // @Override
  // public ValidatorBuilderBoolean<PropertyBuilder<BooleanProperty>> withValdidator() {
  //
  // return withValdidator(x -> new ValidatorBuilderBoolean<>(x));
  // }

}
