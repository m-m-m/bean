/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.github.mmm.bean.impl.BeanClassImpl;

/// [BeanClass] extends [BeanType] for a [VirtualBean] with extended information.
///
/// @see VirtualBean#getType()
/// @since 1.0.0
public interface BeanClass extends BeanType {

  /// @return the [Collection] of the super [BeanClass] this class inherits from.
  Collection<BeanClass> getSuperClasses();

  @Override
  Class<? extends VirtualBean> getJavaClass();

  /// @return the prototype of this [BeanClass]. Properties added to this prototype will be inherited by all
  ///         instances of this [BeanClass] including those created before adding the new property.
  VirtualBean getPrototype();

  /// @param beanClass the [BeanClass] to check hierarchical relationship with.
  /// @return `true` if `this` [BeanClass] is a transitive [super-class][#getSuperClasses()] of
  ///         the given [BeanClass], `false` otherwise.
  default boolean isSuperclassOf(BeanClass beanClass) {

    return isSuperclassOf(beanClass, true, false);
  }

  /// @param beanClass the [BeanClass] to check hierarchical relationship with.
  /// @param transitive `true` to check hierarchy transitive, `false` otherwise (only check for direct
  ///        superclass).
  /// @param equal `true` if equality of `this` and the given [BeanClass] will be accepted (returning
  ///        `true`), `false` otherwise.
  /// @return `true` if `this` [BeanClass] is a (`transitive`) [super-class][#getSuperClasses()] of (or `equal` to) the given [BeanClass], `false` otherwise.
  default boolean isSuperclassOf(BeanClass beanClass, boolean transitive, boolean equal) {

    if (beanClass == null) {
      return false;
    } else if (equal && (this == beanClass)) {
      return true;
    } else if (transitive) {
      for (BeanClass superClass : beanClass.getSuperClasses()) {
        if (superClass.isSuperclassOf(beanClass, true, true)) {
          return true;
        }
      }
    } else {
      for (BeanClass superClass : beanClass.getSuperClasses()) {
        if (superClass == this) {
          return true;
        }
      }
    }
    return false;
  }

  /// @param beanClass the [BeanClass] to check hierarchical relationship with.
  /// @return `true` if `this` [BeanClass] is a transitive sub-class of (or in other words inherits
  ///         from) the given [BeanClass], `false` otherwise.
  default boolean isSubclassOf(BeanClass beanClass) {

    if (beanClass == null) {
      return false;
    }
    return beanClass.isSuperclassOf(this);
  }

  /// @param beanClass the [BeanClass] to check hierarchical relationship with.
  /// @param transitive `true` to check hierarchy transitive, `false` otherwise (only check for direct
  ///        subclass).
  /// @param equal `true` if equality of `this` and the given [BeanClass] will be accepted (returning
  ///        `true`), `false` otherwise.
  /// @return `true` if `this` [BeanClass] is a (`transitive`) sub-class of (or `equal` to)
  ///         the given [BeanClass], `false` otherwise.
  default boolean isSubclassOf(BeanClass beanClass, boolean transitive, boolean equal) {

    if (beanClass == null) {
      return false;
    }
    return beanClass.isSuperclassOf(this, transitive, equal);
  }

  /// @param packageName the [package name][#getPackageName()].
  /// @param simpleName the [simple name][#getSimpleName()].
  /// @param stableName the [stable name][#getStableName()].
  /// @param superClasses the [super-classes][#getSuperClasses()].
  /// @return the created [virtual][#isVirtual()] [BeanClass].
  /// @see AdvancedBean#AdvancedBean(BeanClass)
  @SuppressWarnings({ "unchecked", "rawtypes" })
  static BeanClass createVirtual(String packageName, String simpleName, String stableName, BeanClass... superClasses) {

    List<BeanClassImpl> superClassList = (List) Arrays.asList(superClasses);
    return new BeanClassImpl(superClasses[0].getJavaClass(), superClassList, packageName, stableName, simpleName);
  }

}
