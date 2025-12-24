/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;

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

  private final Map<Class<?>, WritableBean> type2emptyMap;

  private BeanFactoryManager() {

    super();
    this.delegates = new ArrayList<>();
    ServiceLoader<BeanCreator> loader = ServiceLoader.load(BeanCreator.class);
    ServiceHelper.all(loader, this.delegates);
    this.type2emptyMap = new ConcurrentHashMap<>();
  }

  @Override
  public <B extends WritableBean> B getEmpty(Class<B> type) {

    return type.cast(this.type2emptyMap.computeIfAbsent(type, this::createEmpty));
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  private WritableBean createEmpty(Class type) {

    return create(type).getReadOnly();
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
