/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

/**
 * Interface to {@link #create(Class) create} instances of {@link WritableBean}.
 *
 * @since 1.0.0
 * @see BeanFactory
 */
public interface BeanCreator {

  /**
   * Creates a new instance of the {@link WritableBean} for the given {@link Class}.<br>
   * <b>ATTENTION:</b><br>
   * When creating beans from only an interface you will receive a dynamic proxy instance in JVM runtime mode. In
   * environments where dynamic proxies are not available {@code mmm-bean-generator} provides a tooling for you to
   * generate according implementations during AOT compile time (e.g. for GraalVM or TeaVM). In any case some unknown
   * implementation will work behind the scene and therefore you should not rely on the {@link #getClass()} method of a
   * {@link WritableBean bean} instance. Instead you should use {@link ReadableBean#getJavaClass(ReadableBean)} to get
   * the proper expected result.
   *
   * @param <B> type of the {@link WritableBean}.
   * @param type the {@link Class} reflecting the {@link WritableBean}.
   * @return a new instance of the {@link WritableBean} specified by the given {@link Class}. If {@code type} is an
   *         interface, a generated implementation is used (either as dynamic proxy or generated at compile time).
   *         Otherwise if a class is given it needs to extend {@link Bean}, be non-abstract and requires a non-arg
   *         constructor.
   */
  default <B extends WritableBean> B create(Class<B> type) {

    return create(type, null);
  }

  /**
   * @param <B> type of the {@link WritableBean}.
   * @param type the {@link Class} reflecting the {@link WritableBean}.
   * @param beanClass the {@link BeanClass} that has to correspond to the {@link Class} given by parameter {@code type}.
   * @return a new instance of the {@link WritableBean} specified by the given {@link Class}. If {@code type} is an
   *         interface, a dynamic proxy implementation is generated. Otherwise if a class is given it needs to extend
   *         {@link Bean}, be non-abstract and requires a non-arg constructor.
   */
  <B extends WritableBean> B create(Class<B> type, BeanClass beanClass);

}
