/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.AbstractVirtualBean;
import io.github.mmm.bean.Bean;
import io.github.mmm.bean.BeanClass;

/**
 *
 */
public final class SimpleVirtualBean extends AbstractVirtualBean {

  /**
   * The constructor.
   */
  public SimpleVirtualBean() {

    super();
  }

  /**
   * The constructor.
   *
   * @param writable the writable {@link Bean} to create a {@link #isReadOnly() read-only} view on or {@code null} to
   *        create a regular mutable {@link Bean}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   */
  public SimpleVirtualBean(AbstractBean writable, boolean dynamic) {

    super(writable, dynamic);
  }

  /**
   * The constructor.
   *
   * @param writable the writable {@link Bean} to create a {@link #isReadOnly() read-only} view on or {@code null} to
   *        create a regular mutable {@link Bean}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   * @param type the {@link #getType() type}.
   */
  public SimpleVirtualBean(AbstractBean writable, boolean dynamic, BeanClass type) {

    super(writable, dynamic, type);
  }

}
