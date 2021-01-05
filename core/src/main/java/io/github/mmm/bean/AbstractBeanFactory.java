/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Abstract base implementation of {@link BeanFactory} for generated implementations.
 *
 * @since 1.0.0
 */
public abstract class AbstractBeanFactory implements BeanFactory {

  private final Map<Class<?>, Function<BeanClass, WritableBean>> beanFactoryMap;

  /**
   * The constructor.
   */
  public AbstractBeanFactory() {

    super();
    this.beanFactoryMap = new HashMap<>();
  }

  /**
   * @param beanInterface the {@link Class} reflecting the {@link WritableBean} interface to register.
   * @param factory the factory {@link Function} to create instances of that {@link WritableBean} interface.
   */
  protected void add(Class<?> beanInterface, Function<BeanClass, WritableBean> factory) {

    this.beanFactoryMap.put(beanInterface, factory);
  }

  @SuppressWarnings("unchecked")
  @Override
  public <B extends WritableBean> B create(Class<B> type, BeanClass beanClass) {

    Function<BeanClass, WritableBean> factory = this.beanFactoryMap.get(type);
    if (factory != null) {
      return (B) factory.apply(beanClass);
    }
    return null;
  }

}
