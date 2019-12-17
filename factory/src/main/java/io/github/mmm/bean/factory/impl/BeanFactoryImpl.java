/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.util.Objects;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.factory.BeanFactory;
import io.github.mmm.bean.factory.impl.proxy.BeanProxyPrototype;
import io.github.mmm.bean.impl.BeanCreator;

/**
 * Implementation of {@link BeanFactory}.
 */
public class BeanFactoryImpl implements BeanFactory {

  /** The singleton instance. */
  public static final BeanFactoryImpl INSTANCE = new BeanFactoryImpl(Thread.currentThread().getContextClassLoader());

  private final ClassLoader classLoader;

  /**
   * The constructor.
   *
   * @param classLoader the {@link ClassLoader}.
   */
  public BeanFactoryImpl(ClassLoader classLoader) {

    super();
    this.classLoader = classLoader;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public <B extends WritableBean> B create(Class<B> type, boolean dynamic) {

    Objects.requireNonNull(type, "type");
    try {
      if (type.isInterface()) {
        BeanProxyPrototype prototype = BeanProxyPrototype.get(type, this, true);
        return (B) prototype.newInstance(dynamic).getProxy();
      } else {
        return (B) BeanCreator.create((Class) type, null, dynamic);
      }
    } catch (Exception e) {
      throw new IllegalArgumentException("Failed to create bean of type " + type.getName(), e);
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
