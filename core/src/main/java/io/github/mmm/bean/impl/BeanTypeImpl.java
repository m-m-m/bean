/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.impl;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.BeanType;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.impl.properties.BeanProperties;
import io.github.mmm.bean.impl.properties.BeanPropertiesArray;
import io.github.mmm.bean.impl.properties.BeanPropertiesMap;
import io.github.mmm.bean.impl.properties.BeanPropertyNames;

/**
 * Implementation of {@link BeanType}.
 *
 * @see ReadableBean#getType()
 */
public class BeanTypeImpl extends AbstractBeanType {

  private static final Map<String, BeanTypeImpl> CLASS_MAP = new ConcurrentHashMap<>();

  private BeanPropertyNames propertyNames;

  private BeanPropertiesMap properties;

  /**
   * The constructor.
   *
   * @param javaClass the {@link #getJavaClass() java class}.
   * @param stableName the {@link #getStableName() stable name}.
   */
  protected BeanTypeImpl(Class<? extends WritableBean> javaClass, String stableName) {

    super(javaClass, stableName);
  }

  /**
   * The constructor.
   *
   * @param template the template to copy.
   */
  protected BeanTypeImpl(BeanTypeImpl template) {

    super(template);
  }

  @SuppressWarnings("exports")
  @Override
  public BeanProperties create(AbstractBean bean) {

    if (bean.isDynamic()) {
      return new BeanPropertiesMap(BeanAccessor.isThreadSafe(bean));
    } else if (this.properties == null) {
      // use map for prototype (first instance)
      this.properties = new BeanPropertiesMap(BeanAccessor.isThreadSafe(bean));
      return this.properties;
    } else {
      if (this.propertyNames == null) {
        this.propertyNames = this.properties.createNames();
      }
      return new BeanPropertiesArray(this.propertyNames);
    }
  }

  /**
   * @param javaClass the {@link #getJavaClass() java class}.
   * @return the {@link BeanType} representing the given {@link Class}.
   */
  public static BeanTypeImpl asType(Class<? extends WritableBean> javaClass) {

    return asType(javaClass, null);
  }

  /**
   * @param javaClass the {@link #getJavaClass() java class}.
   * @param stableName the custom {@link #getStableName() stable name}.
   * @return the {@link BeanType} representing the given {@link Class}.
   */
  public static BeanTypeImpl asType(Class<? extends WritableBean> javaClass, String stableName) {

    if (javaClass == null) {
      return null;
    }
    return CLASS_MAP.computeIfAbsent(javaClass.getName(), (x) -> new BeanTypeImpl(javaClass, stableName));
  }
}
