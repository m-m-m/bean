/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.BeanClass;
import io.github.mmm.bean.BeanType;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.factory.impl.BeanFactoryImpl;
import io.github.mmm.bean.factory.impl.bean.SimpleBean;
import io.github.mmm.bean.factory.impl.bean.SimpleVirtualBean;
import io.github.mmm.bean.factory.impl.operation.BeanOperation;

/**
 * Proxy for a {@link WritableBean}.
 *
 * @since 1.0.0
 */
public abstract class BeanProxy implements InvocationHandler {

  /** The {@link BeanFactoryImpl}. */
  protected final BeanFactoryImpl beanFactory;

  /** @see #getProxy() */
  protected final WritableBean proxy;

  /** @see #getBean() */
  protected final AbstractBean bean;

  /**
   * The constructor.
   *
   * @param beanFactory the owning {@link BeanFactoryImpl}.
   * @param writable the {@link WritableBean} to wrap as {@link WritableBean#isReadOnly() read-only} bean or
   *        {@code null} to create a mutable bean.
   * @param beanType the {@link BeanType}.
   * @param interfaces the {@link BeanProxyPrototype#getInterfaces() interfaces}.
   */
  public BeanProxy(BeanFactoryImpl beanFactory, WritableBean writable, BeanType beanType, Class<?>... interfaces) {

    super();
    this.proxy = beanFactory.createProxy(this, interfaces);
    this.beanFactory = beanFactory;
    if (beanType instanceof BeanClass) {
      this.bean = new SimpleVirtualBean(writable, (BeanClass) beanType);
    } else {
      this.bean = new SimpleBean(writable, beanType);
    }
  }

  /**
   * The constructor.
   *
   * @param beanFactory the owning {@link BeanFactoryImpl}.
   * @param bean the {@link AbstractBean}.
   * @param interfaces the {@link BeanProxyPrototype#getInterfaces() interfaces}.
   */
  public BeanProxy(BeanFactoryImpl beanFactory, AbstractBean bean, Class<?>... interfaces) {

    super();
    this.proxy = beanFactory.createProxy(this, interfaces);
    this.beanFactory = beanFactory;
    this.bean = bean;
  }

  /**
   * @return the {@link BeanProxyPrototype}.
   */
  public abstract BeanProxyPrototype getPrototype();

  @Override
  public Object invoke(Object instance, Method method, Object[] args) throws Throwable {

    Object result = null;
    BeanOperation operation = getPrototype().getOperation(method);
    if (operation == null) {
      result = method.invoke(this.bean, args);
    } else {
      result = operation.invoke(this, args);
    }
    return result;
  }

  /**
   * @return the dynamic proxy instance.
   */
  public WritableBean getProxy() {

    return this.proxy;
  }

  /**
   * @return the bean class implementation to use as container delegate.
   */
  public AbstractBean getBean() {

    return this.bean;
  }

}
