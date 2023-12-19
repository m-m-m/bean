/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ServiceLoader;

import io.github.mmm.base.service.ServiceHelper;
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
    ServiceHelper.all(loader, this.delegates);
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
      String message = "No BeanFactory available for this bean type.";
      if (this.delegates.size() <= 1) {
        message = message
            + " It seems you did not include dependency mmm-bean-factory or require module io.github.mmm.bean.factory.";
      }
      throw new IllegalStateException(message);
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to create bean of type " + type.getName(), e);
    }
  }

}
