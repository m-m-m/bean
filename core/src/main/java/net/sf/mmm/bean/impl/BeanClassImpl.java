/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import net.sf.mmm.bean.AbstractBean;
import net.sf.mmm.bean.AdvancedBean;
import net.sf.mmm.bean.Bean;
import net.sf.mmm.bean.BeanClass;
import net.sf.mmm.bean.VirtualBean;
import net.sf.mmm.bean.WritableBean;
import net.sf.mmm.property.WritableProperty;

/**
 * A {@link BeanClass} reflects a {@link Bean} (similar to a Java {@link Class}).
 */
public final class BeanClassImpl extends AbstractBean implements BeanClass {

  private static final AtomicLong MODIFICATION_SEQUNCE = new AtomicLong(1);

  private static final Map<String, BeanClassImpl> CLASS_MAP = new ConcurrentHashMap<>();

  private static final BeanClassImpl CLASS = new BeanClassImpl(BeanClass.class, Collections.emptyList(), false);

  private Map<String, String> aliasMap;

  private final Class<? extends VirtualBean> javaClass;

  private final List<BeanClassImpl> superClassList;

  private final List<BeanClass> superClasses;

  private final boolean virtual;

  private final String packageName;

  private final String simpleName;

  private final String stableName;

  private final String qualifiedName;

  private long modificationCounter;

  private long updateCounter;

  /**
   * The constructor.
   *
   * @param javaClass the {@link Class} of the {@link Bean} to reflect.
   * @param superClassList the {@link #getSuperClasses() super-classes}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   */
  public BeanClassImpl(Class<? extends VirtualBean> javaClass, List<BeanClassImpl> superClassList, boolean dynamic) {

    this(javaClass, superClassList, null, dynamic);
  }

  /**
   * The constructor.
   *
   * @param javaClass the {@link Class} of the {@link Bean} to reflect.
   * @param superClassList the {@link #getSuperClasses() super-classes}.
   * @param stableName the {@link #getStableName() stable name}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   */
  public BeanClassImpl(Class<? extends VirtualBean> javaClass, List<BeanClassImpl> superClassList, String stableName,
      boolean dynamic) {

    this(null, javaClass, superClassList, javaClass.getPackageName(), javaClass.getSimpleName(), stableName, dynamic,
        false);
  }

  /**
   * The constructor.
   *
   * @param javaClass the {@link Class} of the {@link Bean} to reflect.
   * @param superClassList the {@link #getSuperClasses() super-classes}.
   * @param stableName the {@link #getStableName() stable name}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   */
  public BeanClassImpl(Class<? extends VirtualBean> javaClass, List<BeanClassImpl> superClassList, String stableName,
      String simpleName, boolean dynamic) {

    this(javaClass, superClassList, javaClass.getPackageName(), simpleName, stableName, dynamic);
  }

  /**
   * The constructor.
   *
   * @param javaClass the {@link Class} of the {@link Bean} to reflect.
   * @param superClassList the {@link #getSuperClasses() super-classes}.
   * @param packageName the {@link #getPackageName() package name}.
   * @param stableName the {@link #getStableName() stable name}.
   * @param simpleName the {@link #getSimpleName() simple name}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   */
  public BeanClassImpl(Class<? extends VirtualBean> javaClass, List<BeanClassImpl> superClassList, String packageName,
      String stableName, String simpleName, boolean dynamic) {

    this(null, javaClass, superClassList, packageName, simpleName, stableName, dynamic, true);
  }

  private BeanClassImpl(BeanClassImpl writable, Class<? extends VirtualBean> javaClass,
      List<BeanClassImpl> superClassList, String packageName, String simpleName, String stableName, boolean dynamic,
      boolean virtual) {

    super(writable, dynamic);
    this.javaClass = javaClass;
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
    if ((stableName == null) || stableName.isBlank()) {
      this.stableName = this.simpleName;
    } else {
      this.stableName = stableName;
    }
    if (this.packageName.isEmpty()) {
      this.qualifiedName = this.simpleName;
    } else {
      this.qualifiedName = this.packageName + "." + this.simpleName;
    }
    this.virtual = virtual;
  }

  private BeanClassImpl(BeanClassImpl writable) {

    super(writable, writable.isDynamic());
    this.javaClass = writable.javaClass;
    this.superClassList = writable.superClassList;
    this.superClasses = writable.superClasses;
    this.packageName = writable.packageName;
    this.simpleName = writable.simpleName;
    this.stableName = writable.stableName;
    this.qualifiedName = writable.qualifiedName;
    this.virtual = writable.virtual;
  }

  @Override
  public BeanClass getType() {

    return CLASS;
  }

  @Override
  protected boolean isThreadSafe() {

    return true;
  }

  @Override
  protected AbstractBean create(AbstractBean writable, boolean dynamic) {

    if (writable == this) {
      return new BeanClassImpl(this);
    } else if (writable == null) {
      if (dynamic == isDynamic()) {
        return this;
      }
      return new BeanClassImpl(null, this.javaClass, this.superClassList, this.packageName, this.simpleName,
          this.stableName, dynamic, this.virtual);
    }
    return super.create(writable, dynamic);
  }

  @Override
  public Class<? extends VirtualBean> getJavaClass() {

    return this.javaClass;
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
  public String getStableName() {

    return this.stableName;
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
  public boolean isClass() {

    return true;
  }

  /**
   * @return the current modification counter of this class.
   */
  public long getModificationCounter() {

    return this.modificationCounter;
  }

  @Override
  protected void onPropertyAdded(WritableProperty<?> property) {

    super.onPropertyAdded(property);
    this.modificationCounter = MODIFICATION_SEQUNCE.incrementAndGet();
  }

  @Override
  protected void updateProperties() {

    super.updateProperties();
    long maxCounter = this.updateCounter;
    for (BeanClassImpl superClass : this.superClassList) {
      if (superClass.isDynamic()) {
        Iterable<? extends WritableProperty<?>> properties = superClass.getProperties();
        if (superClass.modificationCounter > this.updateCounter) {
          for (WritableProperty<?> property : properties) {
            add(property, AddMode.COPY);
          }
          if (superClass.modificationCounter > maxCounter) {
            maxCounter = superClass.modificationCounter;
          }
        }
      }
    }
    this.updateCounter = maxCounter;
  }

  @Override
  public String getPropertyNameForAlias(String alias) {

    if (this.aliasMap != null) {
      return this.aliasMap.get(alias);
    }
    return null;
  }

  /**
   * @param alias the alias.
   * @param name the property name.
   * @see #getPropertyNameForAlias(String)
   */
  public void setAlias(String alias, String name) {

    if (this.aliasMap == null) {
      this.aliasMap = new HashMap<>();
    }
    this.aliasMap.put(alias, name);
  }

  /**
   * @param javaClass the {@link #getJavaClass() java class} reflecting the {@link VirtualBean}.
   * @return the {@link BeanClassImpl} for the given {@link Class}.
   */
  public static BeanClassImpl of(Class<? extends VirtualBean> javaClass) {

    BeanClassImpl beanClass = CLASS_MAP.computeIfAbsent(javaClass.getName(), (x) -> create(javaClass));
    return beanClass;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private static BeanClassImpl create(Class<? extends VirtualBean> type) {

    List<BeanClassImpl> superClassList;
    if (type.isInterface()) {
      Class<?>[] interfaces = type.getInterfaces();
      superClassList = new ArrayList<>(interfaces.length);
      for (Class<?> superclass : interfaces) {
        if (WritableBean.class.isAssignableFrom(superclass)) {
          superClassList.add(of((Class) superclass));
        }
      }
    } else {
      Class<?> superclass = type.getSuperclass();
      if (AdvancedBean.class.isAssignableFrom(superclass)) {
        superClassList = Collections.singletonList(of((Class) superclass));
      } else {
        superClassList = Collections.emptyList();
      }
    }
    return new BeanClassImpl(type, superClassList, true);
  }

}
