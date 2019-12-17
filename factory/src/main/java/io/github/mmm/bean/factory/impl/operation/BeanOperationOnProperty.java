/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.operation;

import java.lang.reflect.Method;
import java.util.Objects;

import io.github.mmm.bean.Bean;
import io.github.mmm.property.WritableProperty;

/**
 * Operation on a {@link Bean} {@link WritableProperty property}.
 *
 * @since 1.0.0
 */
public abstract class BeanOperationOnProperty extends BeanOperation {

  /** @see #getPropertyName() */
  protected final String propertyName;

  /** @see #getMethod() */
  protected final Method method;

  /**
   * The constructor.
   *
   * @param propertyName the {@link #getPropertyName() propertyName}.
   * @param method the {@link #getMethod() method}.
   */
  public BeanOperationOnProperty(String propertyName, Method method) {

    super();
    Objects.requireNonNull(propertyName, "propertyName");
    this.propertyName = propertyName;
    this.method = method;
  }

  @Override
  public String getPropertyName() {

    return this.propertyName;
  }

  /**
   * @return the {@link Method} of this operation.
   */
  public Method getMethod() {

    return this.method;
  }

}
