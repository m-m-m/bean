/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.impl;

import java.util.function.Supplier;

import io.github.mmm.property.AbstractPropertyMetadata;
import io.github.mmm.property.PropertyMetadata;
import io.github.mmm.property.PropertyMetadataType;
import io.github.mmm.validation.Validator;

/**
 * Implementation of {@link BeanPropertyMetadata} extending {@link PropertyMetadataType}.
 *
 * @param <V> type of the {@link io.github.mmm.property.Property#get() property value}.
 * @since 1.0.0
 */
public class BeanPropertyMetadataSimple<V> extends AbstractPropertyMetadata<V> implements BeanPropertyMetadata<V> {

  private Supplier<V> expression;

  @Override
  public Supplier<? extends V> getExpression() {

    return this.expression;
  }

  @Override
  public void makeReadOnly(V value) {

    if (this.expression != null) {
      // should actually be called only once...
      return;
    }
    this.expression = () -> value;
  }

  @Override
  public PropertyMetadata<V> withValidator(Validator<? super V> validator) {

    if (Validator.isValidating(validator)) {
      return new BeanPropertyMetadataType<>(validator, this.expression);
    }
    return this;
  }

}
