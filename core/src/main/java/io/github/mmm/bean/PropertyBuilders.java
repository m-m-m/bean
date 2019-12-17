/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.util.function.Consumer;
import java.util.function.Function;

import io.github.mmm.bean.AbstractBean.AddMode;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.builder.DefaultPropertyBuilders;

/**
 * Implementation of {@link DefaultPropertyBuilders} that auto registers build properties and redirects to read-only
 * properties if {@link AbstractBean#isReadOnly() read-only}.
 */
public class PropertyBuilders
    implements DefaultPropertyBuilders, Consumer<WritableProperty<?>>, Function<String, WritableProperty<?>> {

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
  public WritableProperty<?> apply(String name) {

    if (this.bean.writable != null) {
      return this.bean.writable.getProperty(name).getReadOnly();
    }
    return null;
  }

  @Override
  public void accept(WritableProperty<?> property) {

    this.bean.add(property, AddMode.DIRECT);
  }
}