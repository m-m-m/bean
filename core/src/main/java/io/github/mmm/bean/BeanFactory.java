/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import io.github.mmm.bean.impl.BeanFactoryManager;

/**
 * Interface for a the central factory to create instances of {@link WritableBean}. In case you are implementing beans
 * extending {@link Bean}, you can simply ignore this interface and create your instances with the {@code new} operator.
 * However, framework code should use this interface to create instances for a given class to support the flexibility
 * provided by this module.
 *
 * @since 1.0.0
 */
public interface BeanFactory extends BeanCreator {

  /**
   * @param <B> type of the {@link WritableBean}.
   * @param type the {@link Class} reflecting the {@link WritableBean}.
   * @return the empty {@link WritableBean#isReadOnly() read-only} instance of the {@link WritableBean} specified by the
   *         given {@link Class}. Will be lazily created on the first call of this method. Access is thread-safe.
   */
  <B extends WritableBean> B getEmpty(Class<B> type);

  /**
   * @return the instance of {@link BeanFactory}.
   */
  public static BeanFactory get() {

    return BeanFactoryManager.INSTANCE;
  }

}
