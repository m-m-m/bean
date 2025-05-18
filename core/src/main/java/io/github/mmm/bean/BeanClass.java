/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import io.github.mmm.bean.impl.BeanClassImpl;

/**
 * {@link BeanClass} extends {@link BeanType} for a {@link VirtualBean} with extended information.
 *
 * @see VirtualBean#getType()
 * @since 1.0.0
 */
public interface BeanClass extends BeanType {

  /**
   * @return the {@link Collection} of the super {@link BeanClass} this class inherits from.
   */
  Collection<BeanClass> getSuperClasses();

  @Override
  Class<? extends VirtualBean> getJavaClass();

  /**
   * @return the prototype of this {@link BeanClass}. Properties added to this prototype will be inherited by all
   *         instances of this {@link BeanClass} including those created before adding the new property.
   */
  VirtualBean getPrototype();

  /**
   * @param beanClass the {@link BeanClass} to check hierarchical relationship with.
   * @return {@code true} if {@code this} {@link BeanClass} is a transitive {@link #getSuperClasses() super-class} of
   *         the given {@link BeanClass}, {@code false} otherwise.
   */
  default boolean isSuperclassOf(BeanClass beanClass) {

    return isSuperclassOf(beanClass, true, false);
  }

  /**
   * @param beanClass the {@link BeanClass} to check hierarchical relationship with.
   * @param transitive {@code true} to check hierarchy transitive, {@code false} otherwise (only check for direct
   *        superclass).
   * @param equal {@code true} if equality of {@code this} and the given {@link BeanClass} will be accepted (returning
   *        {@code true}), {@code false} otherwise.
   * @return {@code true} if {@code this} {@link BeanClass} is a ({@code transitive}) {@link #getSuperClasses()
   *         super-class} of (or {@code equal} to) the given {@link BeanClass}, {@code false} otherwise.
   */
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

  /**
   * @param beanClass the {@link BeanClass} to check hierarchical relationship with.
   * @return {@code true} if {@code this} {@link BeanClass} is a transitive sub-class of (or in other words inherits
   *         from) the given {@link BeanClass}, {@code false} otherwise.
   */
  default boolean isSubclassOf(BeanClass beanClass) {

    if (beanClass == null) {
      return false;
    }
    return beanClass.isSuperclassOf(this);
  }

  /**
   * @param beanClass the {@link BeanClass} to check hierarchical relationship with.
   * @param transitive {@code true} to check hierarchy transitive, {@code false} otherwise (only check for direct
   *        subclass).
   * @param equal {@code true} if equality of {@code this} and the given {@link BeanClass} will be accepted (returning
   *        {@code true}), {@code false} otherwise.
   * @return {@code true} if {@code this} {@link BeanClass} is a ({@code transitive}) sub-class of (or {@code equal} to)
   *         the given {@link BeanClass}, {@code false} otherwise.
   */
  default boolean isSubclassOf(BeanClass beanClass, boolean transitive, boolean equal) {

    if (beanClass == null) {
      return false;
    }
    return beanClass.isSuperclassOf(this, transitive, equal);
  }

  /**
   * @param packageName the {@link #getPackageName() package name}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param stableName the {@link #getStableName() stable name}.
   * @param superClasses the {@link #getSuperClasses() super-classes}.
   * @return the created {@link #isVirtual() virtual} {@link BeanClass}.
   * @see AdvancedBean#AdvancedBean(BeanClass)
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  static BeanClass createVirtual(String packageName, String simpleName, String stableName, BeanClass... superClasses) {

    List<BeanClassImpl> superClassList = (List) Arrays.asList(superClasses);
    return new BeanClassImpl(superClasses[0].getJavaClass(), superClassList, packageName, stableName, simpleName);
  }

}
