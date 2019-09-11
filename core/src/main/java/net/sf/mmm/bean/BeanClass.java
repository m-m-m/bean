/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

import java.util.Arrays;
import java.util.List;

import net.sf.mmm.bean.impl.BeanClassImpl;

/**
 * {@link BeanClass} extends {@link BeanType} for a {@link VirtualBean} with extended information. It it also a
 * {@link WritableBean} itself and acts as prototype for the {@link VirtualBean} instances.
 *
 * @see VirtualBean#getType()
 * @since 1.0.0
 */
public interface BeanClass extends BeanType {

  /**
   * @return the {@link List} of the super
   */
  List<BeanClass> getSuperClasses();

  @Override
  Class<? extends VirtualBean> getJavaClass();

  /**
   * @return the prototype of this {@link BeanClass}. Properties added to this prototype will be inherited by all
   *         instances of this {@link BeanClass} including those created before adding the new property.
   */
  VirtualBean getPrototype();

  /**
   * @param packageName the {@link #getPackageName() package name}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param stableName the {@link #getStableName() stable name}.
   * @param superClasses the {@link #getSuperClasses() super-classes}.
   * @return the created {@link #isVirtual() virtual} {@link BeanClass}.
   * @see AdvancedBean#AdvancedBean(AbstractBean, boolean, BeanClass)
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  static BeanClass createVirtual(String packageName, String simpleName, String stableName, BeanClass... superClasses) {

    List<BeanClassImpl> superClassList = (List) Arrays.asList(superClasses);
    return new BeanClassImpl(superClasses[0].getJavaClass(), superClassList, packageName, stableName, simpleName);
  }

}
