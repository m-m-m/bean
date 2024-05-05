/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.util.function.Consumer;
import java.util.function.Function;

import io.github.mmm.bean.AbstractBean.AddMode;
import io.github.mmm.property.AttributeReadOnly;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.builder.PropertyBuilders;

/**
 * Implementation of {@link PropertyBuilders} that auto registers build properties and redirects to read-only properties
 * if {@link AbstractBean#isReadOnly() read-only}.
 */
public class StandardPropertyBuilders
    implements PropertyBuilders, Consumer<WritableProperty<?>>, Function<String, WritableProperty<?>> {

  private final WritableBean bean;

  /**
   * The constructor.
   *
   * @param bean {@link WritableBean} to build the {@link WritableProperty properties} for.
   */
  public StandardPropertyBuilders(WritableBean bean) {

    this.bean = bean;
  }

  @Override
  public void accept(WritableProperty<?> property) {

    if (this.bean instanceof AbstractBean) {
      ((AbstractBean) this.bean).add(property, AddMode.DIRECT);
    } else {
      this.bean.addProperty(property);
    }
  }

  @Override
  public WritableProperty<?> apply(String propertyName) {

    if (this.bean.isReadOnly()) {
      return this.bean.getProperty(propertyName);
    }
    return null;
  }

  @Override
  public AttributeReadOnly getLock() {

    return this.bean;
  }

}