/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.generator;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;

import io.github.mmm.property.factory.PropertyFactory;
import io.github.mmm.property.factory.PropertyFactoryManager;

/**
 * {@link BeanMethod} for a property access method.
 *
 * @since 1.0.0
 */
public class BeanMethodProperty extends BeanMethod {

  /**
   * The constructor.
   *
   * @param method the {@link #getMethod() method}.
   * @param propertyName the {@link #getPropertyName() property name}.
   */
  public BeanMethodProperty(Method method, String propertyName) {

    super(method, propertyName);
  }

  @Override
  protected void writeBody(Writer writer) throws IOException {

    writer.write("    return this.");
    writer.write(this.propertyName);
    writer.write(";\n");
  }

  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Override
  public Class<?> getPropertyType() {

    Class propertyClass = this.method.getReturnType();
    PropertyFactory factory = PropertyFactoryManager.get().getFactoryForPropertyType(propertyClass);
    return factory.getValueClass();
  }

}
