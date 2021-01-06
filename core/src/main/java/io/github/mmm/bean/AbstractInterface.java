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
 * Annotation indicating that a {@link WritableBean}-interface is abstract and can not be instantiated via
 * {@link BeanFactory}. This can avoid generation of an implementation and may save some time during the build process
 * when using the AOT code generator.
 *
 * @see ObjectValidatorBuilder#mandatory()
 * @since 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
@Documented
public @interface AbstractInterface {

}
