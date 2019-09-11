/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.impl;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import net.sf.mmm.bean.Bean;
import net.sf.mmm.bean.BeanClass;
import net.sf.mmm.bean.VirtualBean;
import net.sf.mmm.bean.WritableBean;

/**
 * A {@link BeanClass} reflects a {@link Bean} (similar to a Java {@link Class}).
 */
public final class BeanClassImpl extends BeanTypeImpl implements BeanClass {

  private final List<BeanClassImpl> superClassList;

  private final List<BeanClass> superClasses;

  private final boolean virtual;

  private final String packageName;

  private final String simpleName;

  private final String qualifiedName;

  private VirtualBean prototype;

  private BeanClassImpl readOnly;

  /**
   * The constructor.
   *
   * @param javaClass the {@link Class} of the {@link Bean} to reflect.
   * @param superClassList the {@link #getSuperClasses() super-classes}.
   */
  public BeanClassImpl(Class<? extends VirtualBean> javaClass, List<BeanClassImpl> superClassList) {

    this(javaClass, superClassList, null);
  }

  /**
   * The constructor.
   *
   * @param javaClass the {@link Class} of the {@link Bean} to reflect.
   * @param superClassList the {@link #getSuperClasses() super-classes}.
   * @param stableName the {@link #getStableName() stable name}.
   */
  public BeanClassImpl(Class<? extends VirtualBean> javaClass, List<BeanClassImpl> superClassList, String stableName) {

    this(javaClass, superClassList, javaClass.getPackageName(), javaClass.getSimpleName(), stableName, false);
  }

  /**
   * The constructor.
   *
   * @param javaClass the {@link Class} of the {@link Bean} to reflect.
   * @param superClassList the {@link #getSuperClasses() super-classes}.
   * @param stableName the {@link #getStableName() stable name}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   */
  public BeanClassImpl(Class<? extends VirtualBean> javaClass, List<BeanClassImpl> superClassList, String stableName,
      String simpleName) {

    this(javaClass, superClassList, javaClass.getPackageName(), simpleName, stableName);
  }

  /**
   * The constructor.
   *
   * @param javaClass the {@link Class} of the {@link Bean} to reflect.
   * @param superClassList the {@link #getSuperClasses() super-classes}.
   * @param packageName the {@link #getPackageName() package name}.
   * @param stableName the {@link #getStableName() stable name}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   */
  public BeanClassImpl(Class<? extends VirtualBean> javaClass, List<BeanClassImpl> superClassList, String packageName,
      String stableName, String simpleName) {

    this(javaClass, superClassList, packageName, simpleName, stableName, true);
  }

  private BeanClassImpl(Class<? extends VirtualBean> javaClass, List<BeanClassImpl> superClassList, String packageName,
      String simpleName, String stableName, boolean virtual) {

    super(javaClass, stableName);
    this.superClassList = superClassList;
    this.superClasses = Collections.unmodifiableList(superClassList);
    if (packageName == null) {
      this.packageName = javaClass.getPackageName();
    } else {
      this.packageName = packageName;
    }
    if (simpleName == null) {
      this.simpleName = javaClass.getSimpleName();
    } else {
      this.simpleName = simpleName;
    }
    if (this.packageName.isEmpty()) {
      this.qualifiedName = this.simpleName;
    } else {
      this.qualifiedName = this.packageName + "." + this.simpleName;
    }
    this.virtual = virtual;
  }

  private BeanClassImpl(BeanClassImpl writable) {

    super(writable);
    this.superClassList = writable.superClassList;
    this.superClasses = writable.superClasses;
    this.packageName = writable.packageName;
    this.simpleName = writable.simpleName;
    this.qualifiedName = writable.qualifiedName;
    this.virtual = writable.virtual;
    this.prototype = WritableBean.getReadOnly(writable.prototype);
    this.readOnly = this;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Class<? extends VirtualBean> getJavaClass() {

    return (Class<? extends VirtualBean>) super.getJavaClass();
  }

  @Override
  public List<BeanClass> getSuperClasses() {

    return this.superClasses;
  }

  @Override
  public String getPackageName() {

    return this.packageName;
  }

  @Override
  public String getSimpleName() {

    return this.simpleName;
  }

  @Override
  public String getQualifiedName() {

    return this.qualifiedName;
  }

  @Override
  public boolean isVirtual() {

    return this.virtual;
  }

  @Override
  public VirtualBean getPrototype() {

    return this.prototype;
  }

  /**
   * @param prototype the {@link #getPrototype() prototype}.
   */
  public void setPrototype(VirtualBean prototype) {

    if (this.prototype != null) {
      throw new IllegalStateException("Prototype already set!");
    }
    Objects.requireNonNull(prototype, "prototype");
    this.prototype = prototype;
  }

  public BeanClassImpl getReadOnly() {

    if (this.readOnly == null) {
      this.readOnly = new BeanClassImpl(this);
    }
    return this.readOnly;
  }

}
