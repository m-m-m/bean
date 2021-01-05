/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.generator;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;

/**
 * {@link BeanMethod} for a regular getter method.
 *
 * @since 1.0.0
 */
public class BeanMethodGetter extends BeanMethod {

  /**
   * The constructor.
   *
   * @param method the {@link #getMethod() method}.
   * @param propertyName the {@link #getPropertyName() property name}.
   */
  public BeanMethodGetter(Method method, String propertyName) {

    super(method, propertyName);
  }

  @Override
  protected void writeBody(Writer writer) throws IOException {

    writer.write("    return this.");
    writer.write(this.propertyName);
    writer.write(".get();\n");
  }

  @Override
  public Class<?> getPropertyType() {

    return this.method.getReturnType();
  }

}
