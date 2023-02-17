/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to configure {@link ReadableBean#getAliases() alias mapping} for the annotated
 * {@link io.github.mmm.property.ReadableProperty property}.
 *
 * @see #value()
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface PropertyAlias {

  /**
   * @return if a property method of a {@link WritableBean} interface is annotated the {@link ReadableBean#getAliases()
   *         aliases} of that property.
   * @see ReadableBean#getProperty(String)
   * @see BeanAliasMap#getAliases(String)
   */
  String[] value();

}
