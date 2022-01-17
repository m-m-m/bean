/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.generator;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;

/**
 * {@link BeanMethodContainer} for a regular setter method.
 *
 * @since 1.0.0
 */
public class BeanMethodContainerSetter extends BeanMethodContainer {

  private final Class<?> propertyType;

  /**
   * The constructor.
   *
   * @param method the {@link #getMethod() method}.
   * @param propertyName the {@link #getPropertyName() property name}.
   */
  public BeanMethodContainerSetter(Method method, String propertyName) {

    super(method, propertyName);
    this.propertyType = this.method.getParameterTypes()[0];
  }

  @Override
  protected void writeBody(Writer writer) throws IOException {

    writer.write("    this.");
    writer.write(this.propertyName);
    writer.write(".set(");
    writer.write(getParameterName());
    writer.write(");\n");
  }

  @Override
  protected void writeParameters(Writer writer) throws IOException {

    writer.write(this.propertyType.getSimpleName());
    writer.write(" ");
    writer.write(getParameterName());
  }

  @Override
  public Class<?> getPropertyType() {

    return this.propertyType;
  }
}
