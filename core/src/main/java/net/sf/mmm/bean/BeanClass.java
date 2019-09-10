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
public interface BeanClass extends BeanType, VirtualBean {

  /**
   * @return the {@link List} of the super
   */
  List<BeanClass> getSuperClasses();

  @Override
  Class<? extends VirtualBean> getJavaClass();

  @Override
  default boolean isClass() {

    return true;
  }

  /**
   * @param javaClass the {@link #getJavaClass() java class} reflecting a {@link VirtualBean}.
   * @return the {@link BeanClass} for the given {@link Class}.
   */
  static BeanClass of(Class<? extends VirtualBean> javaClass) {

    return BeanClassImpl.of(javaClass);
  }

  /**
   * @param packageName the {@link #getPackageName() package name}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param stableName the {@link #getStableName() stable name}.
   * @param dynamic the {@link #isDynamic() dyanmic flag}.
   * @param superClasses the {@link #getSuperClasses() super-classes}.
   * @return the created {@link #isVirtual() virtual} {@link BeanClass}.
   * @see AdvancedBean#AdvancedBean(AbstractBean, boolean, BeanClass)
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  static BeanClass createVirtual(String packageName, String simpleName, String stableName, boolean dynamic,
      BeanClass... superClasses) {

    List<BeanClassImpl> superClassList = (List) Arrays.asList(superClasses);
    return new BeanClassImpl(superClasses[0].getJavaClass(), superClassList, packageName, stableName, simpleName,
        dynamic);
  }

  @Override
  default String getPropertyNameForAlias(String alias) {

    return null;
  }

}
