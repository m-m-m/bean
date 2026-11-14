/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import io.github.mmm.base.metainfo.MetaInfo;

/// A [BeanType] reflects a [Bean] (similar to a Java [Class]).
///
/// @see BeanClass
public interface BeanType {

  /// @return the primary [Class] (or interface) reflecting the "implementation" of this [BeanType]. If this
  ///         [BeanType] is [virtual][#isVirtual()] this will be inherited from the first parent class.
  Class<? extends WritableBean> getJavaClass();

  /// @return an array with all types (interfaces) composed (implemented) by this [BeanType].
  default Class<?>[] getJavaClasses() {

    return new Class<?>[] { getJavaClass() };
  }

  /// @return packageName the [package name][Class#getPackageName()].
  /// @see #getJavaClass()
  /// @see Class#getPackageName()
  /// @see #getQualifiedName()
  String getPackageName();

  /// @return the [simple name][Class#getSimpleName()].
  /// @see #getJavaClass()
  /// @see Class#getSimpleName()
  /// @see #getQualifiedName()
  String getSimpleName();

  /// @return qualifiedName the [qualified name][Class#getName()] composed of [package name][#getPackageName()]
  ///         and [simple name][#getSimpleName()].
  /// @see #getPackageName()
  /// @see #getSimpleName()
  /// @see #getJavaClass()
  /// @see Class#getName()
  String getQualifiedName();

  /// @return the stable name of the [Bean] used for external identification (e.g. for un/marshalling polymorphic
  ///         [Bean]s from/to JSON or XML as well as to store type information in databases). It should be short
  ///         like the [simple name][#getSimpleName()] and unique (at least for a single application) like the
  ///         [qualified name][#getQualifiedName()]. This name should be stable so do not change it after it has
  ///         been used externally. The stable name defaults to [simple name][#getSimpleName()] for simplicity but
  ///         may not satisfy stability. To pick a custom stable name you ideally choose a short and stable namespace
  ///         prefix followed by a stable [simple name][#getSimpleName()] without any technical clutter (avoid
  ///         suffixes like `Impl` or `To`). E.g. "`mmm_Address`" for an `Address` [Bean]
  ///         of this project (`io.github.mmm`).
  /// @see BeanName
  String getStableName();

  /// @return `true` if this [BeanType] belongs to a [VirtualBean] meaning that it may represent a
  ///         class or interface that does not exist as Java [Class], `false` otherwise.
  /// @see BeanClass
  /// @see VirtualBean
  boolean isVirtual();

  /// @return the [MetaInfo] of the bean.
  MetaInfo getMetaInfo();

}
