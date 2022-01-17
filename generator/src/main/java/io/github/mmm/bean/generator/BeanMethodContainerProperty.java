/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.generator;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;

/**
 * {@link BeanMethodContainer} for a property access method.
 *
 * @since 1.0.0
 */
public class BeanMethodContainerProperty extends BeanMethodContainer {

  private final Class<?> propertyType;

  /**
   * The constructor.
   *
   * @param method the {@link #getMethod() method}.
   * @param propertyName the {@link #getPropertyName() property name}.
   * @param propertyType the {@link #getPropertyType() property type}.
   */
  public BeanMethodContainerProperty(Method method, String propertyName, Class<?> propertyType) {

    super(method, propertyName);
    this.propertyType = propertyType;
  }

  @Override
  protected void writeBody(Writer writer) throws IOException {

    writer.write("    return this.");
    writer.write(this.propertyName);
    writer.write(";\n");
  }

  @Override
  public Class<?> getPropertyType() {

    return this.propertyType;
  }

}
