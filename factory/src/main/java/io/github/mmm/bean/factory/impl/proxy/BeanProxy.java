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
import io.github.mmm.bean.factory.impl.SimpleBean;
import io.github.mmm.bean.factory.impl.SimpleVirtualBean;
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

  /** @see #isDynamic() */
  protected final boolean dynamic;

  /**
   * The constructor.
   *
   * @param beanFactory the owning {@link BeanFactoryImpl}.
   * @param beanType the {@link BeanType}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   * @param interfaces the {@link BeanProxyPrototype#getInterfaces() interfaces}.
   */
  public BeanProxy(BeanFactoryImpl beanFactory, BeanType beanType, boolean dynamic, Class<?>... interfaces) {

    super();
    this.proxy = beanFactory.createProxy(this, interfaces);
    this.beanFactory = beanFactory;
    this.dynamic = dynamic;
    if (beanType instanceof BeanClass) {
      this.bean = new SimpleVirtualBean((BeanClass) beanType);
    } else {
      this.bean = new SimpleBean(beanType);
    }
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

  /**
   * @return the {@link WritableBean#isDynamic() dynamic} flag.
   */
  public boolean isDynamic() {

    return this.dynamic;
  }

}
