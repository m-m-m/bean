/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

import net.sf.mmm.bean.impl.BeanTypeImpl;

/**
 * Implementation of {@link WritableBean} for regular instances (that are not a {@link BeanClass}).
 */
public class Bean extends AbstractBean {

  private final BeanType type;

  /**
   * The constructor.
   */
  public Bean() {

    this(null, false);
  }

  /**
   * The constructor.
   *
   * @param writable the writable {@link Bean} to create a {@link #isReadOnly() read-only} view on or {@code null} to
   *        create a regular mutable {@link Bean}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   */
  public Bean(AbstractBean writable, boolean dynamic) {

    super(writable, dynamic);
    this.type = BeanTypeImpl.of(getClass());
  }

  @Override
  public BeanType getType() {

    return this.type;
  }

  @Override
  public final boolean isPrototype() {

    return false;
  }

}
