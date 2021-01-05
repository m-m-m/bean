/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.proxy;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import io.github.mmm.bean.AdvancedBean;
import io.github.mmm.bean.BeanType;
import io.github.mmm.bean.VirtualBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.factory.impl.BeanFactoryImpl;
import io.github.mmm.bean.factory.impl.BeanIntrospector;
import io.github.mmm.bean.factory.impl.MemoryCache;
import io.github.mmm.bean.factory.impl.operation.BeanOperation;
import io.github.mmm.bean.impl.BeanClassImpl;
import io.github.mmm.bean.impl.BeanTypeImpl;
import io.github.mmm.property.WritableProperty;

/**
 * {@link BeanProxy} for a {@link WritableBean#isPrototype() prototype}.
 *
 * @since 1.0.0
 */
public class BeanProxyPrototype extends BeanProxy {

  private static final MemoryCache<BeanType, BeanProxyPrototype> cache = new MemoryCache<>();

  private final Map<Method, BeanOperation> method2operationMap;

  private Collection<BeanOperation> propertyOperations;

  /** @see #getBeanType() */
  protected final BeanType beanType;

  /** @see #getInterfaces() */
  protected final Class<?>[] interfaces;

  private boolean baseMethodsInitialized;

  /**
   * The constructor.
   *
   * @param beanFactory the owning {@link BeanFactoryImpl}.
   * @param beanType the {@link BeanType}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   * @param interfaces the {@link #getInterfaces() interfaces}.
   */
  public BeanProxyPrototype(BeanFactoryImpl beanFactory, BeanType beanType, boolean dynamic, Class<?>... interfaces) {

    super(beanFactory, beanType, dynamic, interfaces);
    this.beanType = beanType;
    this.method2operationMap = new HashMap<>();
    this.interfaces = interfaces;
    this.propertyOperations = getPropertyOperations();
    initProperties(this);
  }

  /**
   * @param beanProxy the {@link BeanProxy} where to initialize all properties.
   */
  void initProperties(BeanProxy beanProxy) {

    for (BeanOperation operation : this.propertyOperations) {
      WritableProperty<?> property = operation.createProperty(beanProxy);
      beanProxy.bean.addProperty(property);
    }
  }

  private Collection<BeanOperation> getPropertyOperations() {

    BeanIntrospector introspector = null;
    for (Class<?> beanInterface : this.interfaces) {
      if (introspector == null) {
        introspector = new BeanIntrospector(beanInterface);
      }
      introspect(beanInterface, introspector, true);
    }
    if (introspector == null) {
      return Collections.emptyList();
    } else {
      return introspector.getPropertyOperations();
    }
  }

  private void introspect(Class<?> beanInterface, BeanIntrospector introspector, boolean primary) {

    if ((beanInterface == WritableBean.class) || (beanInterface == VirtualBean.class)) {
      if (!this.baseMethodsInitialized) {
        BeanProxyBaseMethods.INSTANCE.init(this.method2operationMap);
        this.baseMethodsInitialized = true;
      }
      return;
    }
    if (!introspector.visitType(beanInterface)) {
      return;
    }
    for (Method method : beanInterface.getDeclaredMethods()) {
      BeanOperation operation = BeanOperation.create(method, this);
      if (operation != null) {
        this.method2operationMap.put(method, operation);
        if (primary) {
          introspector.add(operation);
        }
      }
    }
    for (Class<?> superInterface : beanInterface.getInterfaces()) {
      introspect(superInterface, introspector, false);
    }
  }

  /**
   * @return an array with optional interfaces to be implemented by the dynamic proxy.
   */
  public Class<?>[] getInterfaces() {

    return this.interfaces;
  }

  /**
   * @return the {@link BeanType}.
   * @see WritableBean#getType()
   */
  public BeanType getBeanType() {

    return this.beanType;
  }

  @Override
  public BeanProxyPrototype getPrototype() {

    return this;
  }

  /**
   * @param method the {@link Method} to lookup.
   * @return the {@link BeanOperation} for the given {@link Method} or {@code null} if not defined.
   */
  protected BeanOperation getOperation(Method method) {

    return this.method2operationMap.get(method);
  }

  /**
   * @param isDynamic the {@link #isDynamic() dynamic} flag.
   * @return the new {@link BeanProxyInstance} of this prototype.
   */
  public BeanProxyInstance newInstance(boolean isDynamic) {

    return new BeanProxyInstance(this, isDynamic);
  }

  /**
   * @param type the {@link BeanType} of the requested prototype.
   * @param beanFactory the {@link BeanFactoryImpl}.
   * @param isDynamic the {@link #isDynamic() dynamic} flag.
   * @return the {@link BeanProxyPrototype} from cache or newly created.
   */
  @SuppressWarnings("unchecked")
  public static BeanProxyPrototype get(Class<? extends WritableBean> type, BeanFactoryImpl beanFactory,
      boolean isDynamic) {

    BeanType beanType;
    if (VirtualBean.class.isAssignableFrom(type)) {
      beanType = BeanClassImpl.getClass(type.getName());
      if (beanType == null) {
        for (Class<?> superType : type.getInterfaces()) {
          if ((VirtualBean.class.isAssignableFrom(superType)) && (VirtualBean.class != superType)) {
            get((Class<? extends WritableBean>) superType, beanFactory, isDynamic);
          }
        }
      }
      beanType = BeanClassImpl.asClass((Class<? extends AdvancedBean>) type);
    } else {
      beanType = BeanTypeImpl.asType(type);
    }
    return get(beanType, beanFactory, isDynamic);
  }

  /**
   * @param type the {@link BeanType} of the requested prototype.
   * @param beanFactory the {@link BeanFactoryImpl}.
   * @param isDynamic the {@link #isDynamic() dynamic} flag.
   * @return the {@link BeanProxyPrototype} from cache or newly created.
   */
  public static BeanProxyPrototype get(BeanType type, BeanFactoryImpl beanFactory, boolean isDynamic) {

    return cache.get(type, () -> new BeanProxyPrototype(beanFactory, type, isDynamic, type.getJavaClasses()));
  }

}
