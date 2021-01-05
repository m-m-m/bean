/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.impl;

import java.lang.reflect.Constructor;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.BeanClass;
import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.WritableBean;

/**
 * Creator of {@link AbstractBean} instances.
 *
 * @since 1.0.0
 * @see #doCreate(Class)
 */
public final class BeanCreator implements BeanFactory {

  /**
   * The constructor.
   */
  public BeanCreator() {

    super();
  }

  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public <B extends WritableBean> B create(Class<B> type, BeanClass beanClass) {

    try {
      if (type.isInterface() || (beanClass != null)) {
        return null;
      }
      return (B) doCreate((Class) type);
    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  /**
   * @param <B> type of {@link AbstractBean}.
   * @param type {@link Class} of {@link AbstractBean}.
   * @return the new bean instance.
   * @throws ReflectiveOperationException in case of an error.
   */
  @SuppressWarnings("unchecked")
  public static <B extends AbstractBean> B doCreate(Class<B> type) throws ReflectiveOperationException {

    Constructor<? extends AbstractBean> constructor = type.getConstructor();
    return (B) constructor.newInstance();
  }

}
