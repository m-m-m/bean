/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.proxy;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.factory.impl.operation.BeanOperation;
import io.github.mmm.bean.factory.impl.operation.BeanOperationCopy;
import io.github.mmm.bean.factory.impl.operation.BeanOperationNewInstance;

/**
 *
 */
public class BeanProxyBaseMethods {

  static final BeanProxyBaseMethods INSTANCE = new BeanProxyBaseMethods();

  private final Map<Method, BeanOperation> method2operationMap;

  private BeanProxyBaseMethods() {

    super();
    this.method2operationMap = new HashMap<>();
    try {
      Method copyMethod = ReadableBean.class.getMethod("copy", new Class[] { boolean.class });
      this.method2operationMap.put(copyMethod, new BeanOperationCopy());
      Method newInstanceMethod = ReadableBean.class.getMethod("newInstance", new Class[0]);
      this.method2operationMap.put(newInstanceMethod, new BeanOperationNewInstance());
    } catch (ReflectiveOperationException e) {
      throw new IllegalStateException("Failed to initialize ReadableBean base methods.", e);
    }
  }

  /**
   * @param map the {@link Map} to initialize with the base methods.
   */
  public void init(Map<Method, BeanOperation> map) {

    map.putAll(this.method2operationMap);
  }

}
