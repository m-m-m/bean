/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import io.github.mmm.base.metainfo.MetaInfo;

/**
 * A {@link BeanType} reflects a {@link Bean} (similar to a Java {@link Class}).
 *
 * @see BeanClass
 */
public interface BeanType {

  /**
   * @return the primary {@link Class} (or interface) reflecting the "implementation" of this {@link BeanType}. If this
   *         {@link BeanType} is {@link #isVirtual() virtual} this will be inherited from the first parent class.
   */
  Class<? extends WritableBean> getJavaClass();

  /**
   * @return an array with all types (interfaces) composed (implemented) by this {@link BeanType}.
   */
  default Class<?>[] getJavaClasses() {

    return new Class<?>[] { getJavaClass() };
  }

  /**
   * @return packageName the {@link Class#getPackageName() package name}.
   * @see #getJavaClass()
   * @see Class#getPackageName()
   * @see #getQualifiedName()
   */
  String getPackageName();

  /**
   * @return the {@link Class#getSimpleName() simple name}.
   * @see #getJavaClass()
   * @see Class#getSimpleName()
   * @see #getQualifiedName()
   */
  String getSimpleName();

  /**
   * @return qualifiedName the {@link Class#getName() qualified name} composed of {@link #getPackageName() package name}
   *         and {@link #getSimpleName() simple name}.
   * @see #getPackageName()
   * @see #getSimpleName()
   * @see #getJavaClass()
   * @see Class#getName()
   */
  String getQualifiedName();

  /**
   * @return the stable name of the {@link Bean} used for external identification (e.g. for un/marshalling polymorphic
   *         {@link Bean}s from/to JSON or XML as well as to store type information in databases). It should be short
   *         like the {@link #getSimpleName() simple name} and unique (at least for a single application) like the
   *         {@link #getQualifiedName() qualified name}. This name should be stable so do not change it after it has
   *         been used externally. The stable name defaults to {@link #getSimpleName() simple name} for simplicity but
   *         may not satisfy stability. To pick a custom stable name you ideally choose a short and stable namespace
   *         prefix followed by a stable {@link #getSimpleName() simple name} without any technical clutter (avoid
   *         suffixes like {@code Impl} or {@code To}). E.g. "{@code mmm_Address}" for an {@code Address} {@link Bean}
   *         of this project ({@code io.github.mmm}).
   * @see BeanName
   */
  String getStableName();

  /**
   * @return {@code true} if this {@link BeanType} belongs to a {@link VirtualBean} meaning that it may represent a
   *         class or interface that does not exist as Java {@link Class}, {@code false} otherwise.
   * @see BeanClass
   * @see VirtualBean
   */
  boolean isVirtual();

  /**
   * @return the {@link MetaInfo} of the bean.
   */
  MetaInfo getMetaInfo();

}
