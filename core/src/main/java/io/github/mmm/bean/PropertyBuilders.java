/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

import io.github.mmm.bean.AbstractBean.AddMode;
import io.github.mmm.bean.impl.BeanPropertyMetadataFactory;
import io.github.mmm.property.PropertyMetadata;
import io.github.mmm.property.PropertyMetadataFactory;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.builder.DefaultPropertyBuilders;
import io.github.mmm.validation.Validator;

/**
 * Implementation of {@link DefaultPropertyBuilders} that auto registers build properties and redirects to read-only
 * properties if {@link AbstractBean#isReadOnly() read-only}.
 */
public class PropertyBuilders
    implements DefaultPropertyBuilders, Consumer<WritableProperty<?>>, PropertyMetadataFactory {

  private final AbstractBean bean;

  /**
   * The constructor.
   *
   * @param bean {@link AbstractBean} to build the {@link WritableProperty properties} for.
   */
  protected PropertyBuilders(AbstractBean bean) {

    this.bean = bean;
  }

  @Override
  public void accept(WritableProperty<?> property) {

    this.bean.add(property, AddMode.DIRECT);
  }

  @Override
  public <V> PropertyMetadata<V> create(Validator<? super V> validator, Supplier<? extends V> expression,
      Type valueType, Map<String, Object> map) {

    return BeanPropertyMetadataFactory.get().create(validator, expression, valueType, map);
  }
}