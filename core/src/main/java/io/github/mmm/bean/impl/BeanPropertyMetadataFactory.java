/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.impl;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Supplier;

import io.github.mmm.property.PropertyMetadata;
import io.github.mmm.property.PropertyMetadataFactory;
import io.github.mmm.validation.Validator;

/**
 * Implementation of {@link PropertyMetadataFactory} for {@link BeanPropertyMetadata}.
 *
 * @since 1.0.0
 */
public class BeanPropertyMetadataFactory implements PropertyMetadataFactory {

  private static final BeanPropertyMetadataFactory INSTANCE = new BeanPropertyMetadataFactory();

  @Override
  public <V> PropertyMetadata<V> create(Validator<? super V> validator, Supplier<? extends V> expression,
      Type valueType, Map<String, Object> map) {

    if (Validator.isValidating(validator) || (expression != null) || (valueType != null) || (map != null)) {
      return new BeanPropertyMetadataType<>(validator, expression, valueType, map);
    }
    return new BeanPropertyMetadataSimple<>();
  }

  /**
   * @return the singleton instance of this {@link BeanPropertyMetadataFactory}.
   */
  public static BeanPropertyMetadataFactory get() {

    return INSTANCE;
  }

}
