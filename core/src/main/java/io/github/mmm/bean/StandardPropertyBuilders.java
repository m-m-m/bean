/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.util.function.Consumer;

import io.github.mmm.bean.AbstractBean.AddMode;
import io.github.mmm.property.AttributeReadOnly;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.builder.PropertyBuilders;

/**
 * Implementation of {@link PropertyBuilders} that auto registers build properties and redirects to read-only
 * properties if {@link AbstractBean#isReadOnly() read-only}.
 */
public class StandardPropertyBuilders implements PropertyBuilders, Consumer<WritableProperty<?>> {

  private final AbstractBean bean;

  /**
   * The constructor.
   *
   * @param bean {@link AbstractBean} to build the {@link WritableProperty properties} for.
   */
  protected StandardPropertyBuilders(AbstractBean bean) {

    this.bean = bean;
  }

  @Override
  public void accept(WritableProperty<?> property) {

    this.bean.add(property, AddMode.DIRECT);
  }

  @Override
  public AttributeReadOnly getLock() {

    return this.bean;
  }

}