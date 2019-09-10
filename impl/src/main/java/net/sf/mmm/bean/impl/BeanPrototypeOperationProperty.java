/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.impl;

import java.lang.reflect.Method;

import net.sf.mmm.bean.BeanAccessPrototype;
import net.sf.mmm.bean.api.Bean;
import net.sf.mmm.property.api.WritableProperty;

/**
 * Operation on a {@link Bean} the get the raw {@link WritableProperty property}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public class BeanPrototypeOperationProperty extends BeanPrototypeOperationOnProperty {

  /**
   * The constructor.
   *
   * @param prototype the {@link BeanAccessPrototype}.
   * @param method the {@link Method}.
   * @param property the {@link BeanPrototypeProperty}.
   */
  public BeanPrototypeOperationProperty(BeanAccessPrototype<?> prototype, Method method,
      BeanPrototypeProperty property) {
    super(prototype, method, property);
  }

  @Override
  protected Object doInvoke(WritableProperty<?> property, Object[] args) {

    if (property == null) {
      return null;
    }
    return property;
  }

  @Override
  public BeanPrototypeOperation forPrototype(BeanAccessPrototype<?> prototype) {

    return new BeanPrototypeOperationProperty(prototype, getMethod(),
        prototype.getPrototypeProperty(getProperty().getProperty().getName()));
  }

}
