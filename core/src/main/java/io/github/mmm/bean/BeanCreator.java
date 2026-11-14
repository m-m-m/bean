/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

/// Interface to [create][#create(Class)] instances of [WritableBean].
///
/// @since 1.0.0
/// @see BeanFactory
public interface BeanCreator {

  /// Creates a new instance of the [WritableBean] for the given [Class].
  /// **ATTENTION:**
  /// When creating beans from only an interface you will receive a dynamic proxy instance in JVM runtime mode. In
  /// environments where dynamic proxies are not available `mmm-bean-generator` provides a tooling for you to
  /// generate according implementations during AOT compile time (e.g. for GraalVM or TeaVM). In any case some unknown
  /// implementation will work behind the scene and therefore you should not rely on the [#getClass()] method of a
  /// [bean][WritableBean] instance. Instead you should use [ReadableBean#getJavaClass(ReadableBean)] to get
  /// the proper expected result.
  ///
  /// @param <B> type of the [WritableBean].
  /// @param type the [Class] reflecting the [WritableBean].
  /// @return a new instance of the [WritableBean] specified by the given [Class]. If `type` is an
  ///         interface, a generated implementation is used (either as dynamic proxy or generated at compile time).
  ///         Otherwise if a class is given it needs to extend [Bean], be non-abstract and requires a non-arg
  ///         constructor.
  default <B extends WritableBean> B create(Class<B> type) {

    return create(type, null);
  }

  /// @param <B> type of the [WritableBean].
  /// @param type the [Class] reflecting the [WritableBean].
  /// @param beanClass the [BeanClass] that has to correspond to the [Class] given by parameter `type`.
  /// @return a new instance of the [WritableBean] specified by the given [Class]. If `type` is an
  ///         interface, a dynamic proxy implementation is generated. Otherwise if a class is given it needs to extend
  ///         [Bean], be non-abstract and requires a non-arg constructor.
  <B extends WritableBean> B create(Class<B> type, BeanClass beanClass);

  /// @param <B> type of the [WritableBean].
  /// @param type the [Class] reflecting the [WritableBean].
  /// @return the empty [read-only][WritableBean#isReadOnly()] instance of the [WritableBean] specified by the
  ///         given [Class]. Will be lazily created on the first call of this method. Access is thread-safe.
  <B extends WritableBean> B getEmpty(Class<B> type);

}
