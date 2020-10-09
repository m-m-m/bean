/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.impl;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Supplier;

import io.github.mmm.property.PropertyMetadataType;
import io.github.mmm.validation.Validator;

/**
 * Implementation of {@link BeanPropertyMetadata} extending {@link PropertyMetadataType}.
 *
 * @param <V> type of the {@link io.github.mmm.property.Property#get() property value}.
 * @since 1.0.0
 */
public class BeanPropertyMetadataType<V> extends PropertyMetadataType<V> implements BeanPropertyMetadata<V> {

  /**
   * The constructor.
   *
   * @param validator the {@link #getValidator() validator}.
   * @param expression the {@link #getExpression() expression}.
   */
  public BeanPropertyMetadataType(Validator<? super V> validator, Supplier<? extends V> expression) {

    super(validator, expression, null, null);
  }

  /**
   * The constructor.
   *
   * @param validator the {@link #getValidator() validator}.
   * @param expression the {@link #getExpression() expression}.
   * @param valueType the {@link #getValueType() value type}.
   * @param metadata the {@link #get(String) metadata}.
   */
  public BeanPropertyMetadataType(Validator<? super V> validator, Supplier<? extends V> expression, Type valueType,
      Map<String, Object> metadata) {

    super(validator, expression, valueType, metadata);
  }

  @Override
  public void makeReadOnly(V value) {

    if (this.expression != null) {
      // should actually be called only once...
      return;
    }
    this.expression = () -> value;
  }

}
