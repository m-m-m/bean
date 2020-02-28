/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import io.github.mmm.bean.impl.BeanFactoryManager;

/**
 * Interface for a factory to create instances of {@link WritableBean}. In case you are implementing beans extending
 * {@link Bean}, you can simply ignore this interface and create your instances with the {@code new} operator. However,
 * framework code should use this interface to create instances for a given class to support the flexibility provided by
 * this module.
 *
 * @since 1.0.0
 */
public interface BeanFactory {

  /**
   * @param <B> type of the {@link WritableBean}.
   * @param type the {@link Class} reflecting the {@link WritableBean}.
   * @return a new instance of the {@link WritableBean} specified by the given {@link Class}. If {@code type} is an
   *         interface, a dynamic proxy implementation is generated. Otherwise if a class is given it needs to extend
   *         {@link Bean}, be non-abstract and requires a non-arg constructor.
   */
  default <B extends WritableBean> B create(Class<B> type) {

    return create(type, false);
  }

  /**
   * @param <B> type of the {@link WritableBean}.
   * @param type the {@link Class} reflecting the {@link WritableBean}.
   * @param dynamic the {@link WritableBean#isDynamic() dynamic} flag.
   * @return a new instance of the {@link WritableBean} specified by the given {@link Class}. If {@code type} is an
   *         interface, a dynamic proxy implementation is generated. Otherwise if a class is given it needs to extend
   *         {@link Bean}, be non-abstract and requires a non-arg constructor.
   */
  <B extends WritableBean> B create(Class<B> type, boolean dynamic);

  /**
   * @return the instance of {@link BeanFactory}.
   */
  public static BeanFactory get() {

    return BeanFactoryManager.INSTANCE;
  }

}
