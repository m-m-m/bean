/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.property;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.PropertyMetadata;
import io.github.mmm.property.builder.PropertyBuilder;
import io.github.mmm.property.builder.PropertyBuilders;
import io.github.mmm.validation.main.ValidatorBuilderObject;

/**
 * {@link PropertyBuilder} for {@link BeanProperty}.
 *
 * @param <V> type of the {@link WritableBean}.
 * @since 1.0.0
 */
public class BeanPropertyBuilder<V extends WritableBean> extends
    PropertyBuilder<V, BeanProperty<V>, ValidatorBuilderObject<V, BeanPropertyBuilder<V>>, BeanPropertyBuilder<V>> {

  private Class<V> valueClass;

  /**
   * The constructor.
   *
   * @param parent the parent {@link PropertyBuilders}.
   */
  public BeanPropertyBuilder(PropertyBuilders parent) {

    this(parent, null);
  }

  /**
   * The constructor.
   *
   * @param parent the parent {@link PropertyBuilders}.
   * @param valueClass the {@link #valueClass(Class) value class}.
   */
  public BeanPropertyBuilder(PropertyBuilders parent, Class<V> valueClass) {

    super(parent);
    this.valueClass = valueClass;
  }

  /**
   * @param valueType the {@link BeanProperty#getValueClass() value class}.
   * @return this builder itself ({@code this}) for fluent API calls.
   */
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
