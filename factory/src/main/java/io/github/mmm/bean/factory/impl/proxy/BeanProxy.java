/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.BeanClass;
import io.github.mmm.bean.BeanType;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.factory.impl.BeanInterfaceCreator;
import io.github.mmm.bean.factory.impl.bean.InternalBean;
import io.github.mmm.bean.factory.impl.bean.SimpleBean;
import io.github.mmm.bean.factory.impl.bean.SimpleVirtualBean;
import io.github.mmm.bean.factory.impl.operation.BeanOperation;

/// Proxy for a [WritableBean].
///
/// @since 1.0.0
public abstract class BeanProxy implements InvocationHandler {

  /// The [BeanInterfaceCreator].
  protected final BeanInterfaceCreator beanFactory;

  /// @see #getProxy()
  protected final WritableBean proxy;

  /// @see #getBean()
  protected final InternalBean bean;

  private BeanProxy readOnly;

  /// The constructor.
  ///
  /// @param beanFactory the owning [BeanInterfaceCreator].
  /// @param writable the [WritableBean] to wrap as [read-only][WritableBean#isReadOnly()] bean or
  ///        `null` to create a mutable bean.
  /// @param beanType the [BeanType].
  /// @param interfaces the [interfaces][BeanProxyPrototype#getInterfaces()].
  public BeanProxy(BeanInterfaceCreator beanFactory, WritableBean writable, BeanType beanType, Class<?>... interfaces) {

    super();
    this.proxy = beanFactory.createProxy(this, interfaces);
    this.beanFactory = beanFactory;
    if (beanType instanceof BeanClass) {
      this.bean = new SimpleVirtualBean(writable, (BeanClass) beanType, this.proxy);
    } else {
      this.bean = new SimpleBean(writable, beanType, this.proxy);
    }
    if (writable != null) {
      this.readOnly = this;
    }
  }

  /// The constructor.
  ///
  /// @param beanFactory the owning [BeanInterfaceCreator].
  /// @param bean the [AbstractBean].
  /// @param interfaces the [interfaces][BeanProxyPrototype#getInterfaces()].
  public BeanProxy(BeanInterfaceCreator beanFactory, InternalBean bean, Class<?>... interfaces) {

    super();
    this.proxy = beanFactory.createProxy(this, interfaces);
    this.beanFactory = beanFactory;
    this.bean = bean;
  }

  /// @return the [BeanProxyPrototype].
  public abstract BeanProxyPrototype getPrototype();

  /// @return the [read-only instance][WritableBean#getReadOnly()].
  public BeanProxy getReadOnly() {

    if (this.readOnly == null) {
      this.readOnly = new BeanProxyInstance(this);
    }
    return this.readOnly;
  }

  @Override
  public Object invoke(Object instance, Method method, Object[] args) throws Throwable {

    Object result = null;
    BeanOperation operation = getPrototype().getOperation(method);
    if (operation == null) {
      result = method.invoke(this.bean, args);
    } else {
      result = operation.invoke(this, args);
    }
    if (result == this.bean) {
      result = this.proxy;
    }
    return result;
  }

  /// @return the dynamic proxy instance.
  public WritableBean getProxy() {

    return this.proxy;
  }

  /// @return the bean class implementation to use as container delegate.
  public InternalBean getBean() {

    return this.bean;
  }

}
