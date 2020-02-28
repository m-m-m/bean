/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.factory.impl.proxy.BeanProxyPrototype;

/**
 * Implementation of {@link BeanFactory}.
 */
public class BeanFactoryImpl implements BeanFactory {

  private final ClassLoader classLoader;

  /**
   * The constructor.
   */
  public BeanFactoryImpl() {

    this(Thread.currentThread().getContextClassLoader());
  }

  /**
   * The constructor.
   *
   * @param classLoader the {@link ClassLoader}.
   */
  public BeanFactoryImpl(ClassLoader classLoader) {

    super();
    this.classLoader = classLoader;
  }

  @SuppressWarnings({ "unchecked" })
  @Override
  public <B extends WritableBean> B create(Class<B> type, boolean dynamic) {

    if (type.isInterface()) {
      BeanProxyPrototype prototype = BeanProxyPrototype.get(type, this, true);
      return (B) prototype.newInstance(dynamic).getProxy();
    } else {
      return null;
    }
  }

  /**
   * @param <B> type of {@link WritableBean}.
   * @param handler the {@link InvocationHandler}.
   * @param interfaces the interfaces to be implemented by the dynamic proxy.
   * @return the dynamic proxy instance.
   */
  @SuppressWarnings("unchecked")
  public <B extends WritableBean> B createProxy(InvocationHandler handler, Class<?>... interfaces) {

    B bean = (B) Proxy.newProxyInstance(this.classLoader, interfaces, handler);
    return bean;
  }

}
