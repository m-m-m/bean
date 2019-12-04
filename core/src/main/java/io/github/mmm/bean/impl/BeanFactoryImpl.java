/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.impl;

import java.util.Objects;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.WritableBean;

/**
 * Implementation of {@link BeanFactory}.
 */
public class BeanFactoryImpl implements BeanFactory {

  /** The singleton instance. */
  public static final BeanFactoryImpl INSTANCE = new BeanFactoryImpl();

  /**
   * The constructor.
   */
  public BeanFactoryImpl() {

    super();
  }

  @Override
  public <B extends WritableBean> B create(Class<B> type) {

    Objects.requireNonNull(type, "type");
    try {
      if (type.isInterface()) {
        // TODO
        return null;
      } else {
        return type.getConstructor().newInstance();
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to create bean of type " + type.getName(), e);
    }
  }

}
