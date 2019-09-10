/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.old;

import net.sf.mmm.bean.api.Bean;
import net.sf.mmm.bean.api.BeanFactory;
import net.sf.mmm.bean.impl.BeanFactoryImpl;

/**
 * This is the implementation of {@link net.sf.mmm.bean.old.api.BeanAccess} for the internal master
 * {@link BeanFactory#createPrototype(Class) prototype} of a {@link MagicBean}.
 *
 * @param <BEAN> the generic type of the {@link MagicBean}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public class BeanAccessPrototypeInternal<BEAN extends MagicBean> extends BeanAccessPrototype<BEAN> {

  /**
   * The constructor.
   *
   * @param beanClass - see {@link #getBeanClass()}.
   * @param qualifiedName - see {@link #getQualifiedName()}.
   * @param beanFactory the owning {@link BeanFactoryImpl}.
   */
  public BeanAccessPrototypeInternal(Class<BEAN> beanClass, String qualifiedName, BeanFactoryImpl beanFactory) {
    super(beanClass, qualifiedName, beanFactory);
  }

  @Override
  public boolean isDynamic() {

    return false;
  }

}
