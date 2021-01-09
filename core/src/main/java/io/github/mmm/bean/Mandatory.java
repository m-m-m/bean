/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import io.github.mmm.validation.main.ObjectValidatorBuilder;

/**
 * Annotation indicating that a property is mandatory. This annotation can not be combined with property default methods
 * that act as factory for the property. It is a convenience annotation to avoid the burden of implementing a default
 * method just for the sake of making a property mandatory what is a quite common case.
 *
 * @see ObjectValidatorBuilder#mandatory()
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface Mandatory {

}
