/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.impl;

import java.lang.reflect.Constructor;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.WritableBean;

/**
 * Creator of {@link AbstractBean} instances.
 *
 * @since 1.0.0
 * @see #create(Class, AbstractBean, boolean)
 */
public final class BeanCreator implements BeanFactory {

  private static final Class<?>[] CONSTRUCTOR_SIGNATURE = new Class<?>[] { AbstractBean.class, boolean.class };

  /**
   * The constructor.
   */
  public BeanCreator() {

    super();
  }

  @Override
  @SuppressWarnings({ "unchecked", "rawtypes" })
  public <B extends WritableBean> B create(Class<B> type, boolean dynamic) {

    try {
      if (type.isInterface()) {
        return null;
      }
      return (B) create((Class) type, null, dynamic);
    } catch (Exception e) {
      throw new IllegalStateException(e.getMessage(), e);
    }
  }

  /**
   * @param <B> type of {@link AbstractBean}.
   * @param type {@link Class} of {@link AbstractBean}.
   * @param writableBean the {@link AbstractBean} to create a {@link AbstractBean#getReadOnly() read-only} copy of.
   * @param dynamicFlag the {@link AbstractBean#isDynamic() dynamic flag}.
   * @return the new bean instance.
   * @throws ReflectiveOperationException in case of an error.
   */
  @SuppressWarnings("unchecked")
  public static <B extends AbstractBean> B create(Class<B> type, AbstractBean writableBean, boolean dynamicFlag)
      throws ReflectiveOperationException {

    Constructor<? extends AbstractBean> constructor = type.getConstructor(CONSTRUCTOR_SIGNATURE);
    return (B) constructor.newInstance(writableBean, Boolean.valueOf(dynamicFlag));
  }

}
