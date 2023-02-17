/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to configure {@link BeanType#getStableName() stable name} of a {@link WritableBean bean interface}.
 *
 * @see #value()
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface BeanName {

  /**
   * @return the value that will be used as {@link BeanType#getStableName() stable name} of a {@link WritableBean bean}
   *         interface or class.
   */
  String value();

}
