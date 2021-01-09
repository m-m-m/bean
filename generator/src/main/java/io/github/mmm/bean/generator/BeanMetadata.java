/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.generator;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;

import io.github.mmm.bean.BeanClass;
import io.github.mmm.bean.VirtualBean;
import io.github.mmm.bean.WritableBean;

/**
 * Collector for introspection data of {@link WritableBean beans}.
 *
 * @since 1.0.0
 */
public abstract class BeanMetadata {

  /** @see #getBeanType() */
  protected final Class<? extends WritableBean> beanType;

  /** @see #isVirtual() */
  protected final boolean virtual;

  /**
   * The constructor.
   *
   * @param beanClass the {@link #getBeanType() bean type}.
   */
  protected BeanMetadata(Class<? extends WritableBean> beanClass) {

    super();
    this.beanType = beanClass;
    this.virtual = VirtualBean.class.isAssignableFrom(this.beanType);
  }

  /**
   * @return the {@link Class} reflecting the {@link WritableBean} this metadata is about.
   */
  public Class<? extends WritableBean> getBeanType() {

    return this.beanType;
  }

  /**
   * @return {@code true} if the {@link #getBeanType() bean type} extends {@link VirtualBean}.
   */
  public boolean isVirtual() {

    return this.virtual;
  }

  /**
   * Writes the entire Java file.
   *
   * @param writer the {@link Writer}.
   * @param beanClassVariable the variable of the {@link BeanClass} to potentially pass to the {@link Constructor}.
   * @throws IOException on error.
   */
  public abstract void writeInstantiation(Writer writer, String beanClassVariable) throws IOException;

}
