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
 * @see #doCreate(Class, WritableBean)
 */
public final class BeanCreator implements BeanFactory {

  private static final Class<?>[] CONSTRUCTOR_SIGNATURE = new Class<?>[] { WritableBean.class };

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
      return (B) doCreate((Class) type, null);
    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  /**
   * @param <B> type of {@link AbstractBean}.
   * @param type {@link Class} of {@link AbstractBean}.
   * @param writable the {@link AbstractBean} to wrap as {@link AbstractBean#isReadOnly() read-only} bean or
   *        {@code null} to create a mutable bean.
   * @return the new bean instance.
   * @throws ReflectiveOperationException in case of an error.
   */
  @SuppressWarnings("unchecked")
  public static <B extends AbstractBean> B doCreate(Class<B> type, WritableBean writable)
      throws ReflectiveOperationException {

    Constructor<? extends AbstractBean> constructor;
    try {
      constructor = type.getConstructor(CONSTRUCTOR_SIGNATURE);
      return (B) constructor.newInstance(writable);
    } catch (NoSuchMethodException e) {
      if (writable == null) {
        constructor = type.getConstructor();
        return (B) constructor.newInstance();
      }
      throw e;
    }
  }

}
