/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

import io.github.mmm.bean.BeanClass;
import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.WritableBean;

/**
 * Implementation of {@link BeanFactory}.
 */
public final class BeanFactoryManager implements BeanFactory {

  /** The singleton instance. */
  public static final BeanFactory INSTANCE = new BeanFactoryManager();

  private final List<BeanFactory> delegates;

  private BeanFactoryManager() {

    super();
    this.delegates = new ArrayList<>();
    ServiceLoader<BeanFactory> loader = ServiceLoader.load(BeanFactory.class);
    for (BeanFactory factory : loader) {
      this.delegates.add(factory);
    }
  }

  @Override
  public <B extends WritableBean> B create(Class<B> type, BeanClass beanClass) {

    Objects.requireNonNull(type, "type");
    try {
      for (BeanFactory delegate : this.delegates) {
        B bean = delegate.create(type, beanClass);
        if (bean != null) {
          return bean;
        }
      }
      throw new IllegalStateException();
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to create bean of type " + type.getName(), e);
    }
  }

}
