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
   * @return the instance of {@link BeanFactory}.
   */
  public static BeanFactory get() {

    return BeanFactoryManager.INSTANCE;
  }

}
