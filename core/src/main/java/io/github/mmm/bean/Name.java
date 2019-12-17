/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to configure {@link BeanType#getStableName() stable name} or
 * {@link ReadableBean#getPropertyNameForAlias(String) alias}.
 *
 * @see #value()
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE, ElementType.METHOD })
@Documented
public @interface Name {

  /**
   * @return if a type is annotated, the {@link BeanType#getStableName() stable name} of a {@link WritableBean bean}
   *         interface or class. In case a property method of a {@link WritableBean} interface is annotated the
   *         {@link ReadableBean#getPropertyNameForAlias(String) alias} of that property.
   */
  String value();

}
