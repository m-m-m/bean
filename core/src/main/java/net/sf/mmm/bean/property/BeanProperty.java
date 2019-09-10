/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.property;

import net.sf.mmm.bean.Bean;
import net.sf.mmm.property.Property;
import net.sf.mmm.property.PropertyMetadata;

/**
 * Implementation of {@link BeanProperty}.
 *
 * @since 1.0.0
 */
public class BeanProperty extends Property<Bean> implements WritableBeanProperty {

  private Bean value;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   */
  public BeanProperty(String name) {

    super(name);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param metadata the {@link #getMetadata() metadata}.
   */
  public BeanProperty(String name, PropertyMetadata<Bean> metadata) {

    super(name, metadata);
  }

  @Override
  protected Bean doGetValue() {

    return this.value;
  }

  @Override
  protected void doSetValue(Bean newValue) {

    this.value = newValue;
  }

  // @Override
  // public ValidatorBuilderBoolean<PropertyBuilder<BooleanProperty>> withValdidator() {
  //
  // return withValdidator(x -> new ValidatorBuilderBoolean<>(x));
  // }

}
