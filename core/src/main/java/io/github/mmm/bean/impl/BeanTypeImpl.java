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
import io.github.mmm.bean.impl.properties.BeanPropertiesDynamicArray;
import io.github.mmm.bean.impl.properties.BeanPropertiesMap;
import io.github.mmm.bean.impl.properties.BeanPropertiesStaticArray;
import io.github.mmm.bean.impl.properties.BeanPropertyNames;

/**
 * Implementation of {@link BeanType}.
 *
 * @see ReadableBean#getType()
 */
public class BeanTypeImpl extends AbstractBeanType {

  private static final Map<String, BeanTypeImpl> CLASS_MAP = new ConcurrentHashMap<>();

  private BeanPropertyNames propertyNames;

  private BeanProperties properties;

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

    boolean threadSafe = BeanAccessor.isThreadSafe(bean);
    boolean dynamic = bean.isDynamic();
    BeanProperties result;
    if (!dynamic && this.properties != null) {
      if (this.propertyNames == null) {
        this.propertyNames = this.properties.createNames();
      }
      result = new BeanPropertiesStaticArray(this.propertyNames);
    } else {
      if (threadSafe) {
        return new BeanPropertiesMap(true);
      } else {
        int capacity = 8;
        if (this.properties != null) {
          capacity = this.properties.get().size() + 2;
        }
        result = new BeanPropertiesDynamicArray(capacity);
      }
      if (this.properties == null) {
        // store for prototype (first instance)
        this.properties = result;
      }
    }
    return result;
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
