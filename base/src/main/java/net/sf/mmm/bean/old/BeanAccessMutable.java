/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.old;

import net.sf.mmm.bean.api.Bean;
import net.sf.mmm.bean.api.BeanAccess;
import net.sf.mmm.bean.impl.BeanFactoryImpl;
import net.sf.mmm.bean.impl.BeanPrototypeProperty;
import net.sf.mmm.property.api.AbstractProperty;
import net.sf.mmm.property.api.WritableProperty;

/**
 * The implementation of {@link BeanAccess} for a regular mutable {@link MagicBean} instance.
 *
 * @param <BEAN> the generic type of the intercepted {@link #getBean() bean}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public class BeanAccessMutable<BEAN extends MagicBean> extends BeanAccessInstance<BEAN> {

  /**
   * The constructor.
   *
   * @param beanFactory the owning {@link BeanFactoryImpl}.
   * @param prototype the {@link BeanAccessPrototype}.
   */
  public BeanAccessMutable(BeanFactoryImpl beanFactory, BeanAccessPrototype<BEAN> prototype) {

    super(beanFactory, prototype);
  }

  @Override
  protected WritableProperty<?> createProperty(BeanClassProperty prototypeProperty) {

    AbstractProperty<?> property = prototypeProperty.getProperty();
    return property.copy(getBean());
  }

}