/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

import io.github.mmm.base.service.ServiceHelper;
import io.github.mmm.bean.BeanClass;
import io.github.mmm.bean.BeanCreator;
import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.WritableBean;

/**
 * Implementation of {@link BeanFactory}.
 */
public final class BeanFactoryManager implements BeanFactory {

  /** The singleton instance. */
  public static final BeanFactory INSTANCE = new BeanFactoryManager();

  private final List<BeanCreator> delegates;

  private BeanFactoryManager() {

    super();
    this.delegates = new ArrayList<>();
    ServiceLoader<BeanCreator> loader = ServiceLoader.load(BeanCreator.class);
    ServiceHelper.all(loader, this.delegates);
  }

  @Override
  public <B extends WritableBean> B create(Class<B> type, BeanClass beanClass) {

    Objects.requireNonNull(type, "type");
    try {
      for (BeanCreator creator : this.delegates) {
        B bean = creator.create(type, beanClass);
        if (bean != null) {
          return bean;
        }
      }
      throw failNoBeanFactory(type);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to create bean of type " + type.getName(), e);
    }
  }

  @Override
  public <B extends WritableBean> B getEmpty(Class<B> type) {

    Objects.requireNonNull(type, "type");
    try {
      for (BeanCreator creator : this.delegates) {
        B bean = creator.getEmpty(type);
        if (bean != null) {
          return bean;
        }
      }
      throw failNoBeanFactory(type);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to get empty bean of type " + type.getName(), e);
    }
  }

  private RuntimeException failNoBeanFactory(Class<?> type) {

    String message = "No BeanFactory available for bean type: " + type;
    if (this.delegates.size() <= 1) {
      message = message
          + " It seems you did not include dependency mmm-bean-factory or require module io.github.mmm.bean.factory.";
    }
    throw new IllegalStateException(message);
  }

}
