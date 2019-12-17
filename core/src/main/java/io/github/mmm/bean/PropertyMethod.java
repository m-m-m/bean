/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation indicating that a default method of a {@link WritableBean} interface is a factory method for a
 * {@link io.github.mmm.property.Property}. When {@code BeanFactory} is used to instantiate {@link WritableBean}s from
 * interface as dynamic {@link java.lang.reflect.Proxy}, such annotated default method will be used only internally to
 * create the {@link io.github.mmm.property.Property} that is then stored in the dynamic proxy. Further invocations of
 * the annotated default method will then return the exact same instance instead of calling the default method
 * again.<br>
 * Please note that we put the annotation into this module for simplicity so your API can depend on less modules even
 * though it technically belongs to {@code mmm-bean-factory}.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
@Documented
public @interface PropertyMethod {

}
