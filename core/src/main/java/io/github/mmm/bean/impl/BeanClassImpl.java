/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.AdvancedBean;
import io.github.mmm.bean.Bean;
import io.github.mmm.bean.BeanClass;
import io.github.mmm.bean.VirtualBean;
import io.github.mmm.bean.impl.properties.BeanProperties;
import io.github.mmm.bean.impl.properties.BeanPropertiesMap;

/**
 * A {@link BeanClass} reflects a {@link Bean} (similar to a Java {@link Class}).
 */
public final class BeanClassImpl extends AbstractBeanType implements BeanClass {

  private static final Map<String, BeanClassImpl> CLASS_MAP = new ConcurrentHashMap<>();

  private final List<BeanClassImpl> superClassList;

  private final List<BeanClass> superClasses;

  private Class<?>[] javaClasses;

  private final boolean virtual;

  private final String packageName;

  private final String simpleName;

  private final String qualifiedName;

  private VirtualBean prototype;

  static {
    asClass(VirtualBean.class);
  }

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
    if (this.superClassList.isEmpty() || !virtual) {
      this.javaClasses = new Class<?>[] { javaClass };
    } else {
      List<Class<?>> classes = new ArrayList<>();
      classes.add(javaClass);
      for (BeanClassImpl superClass : superClassList) {
        for (Class<?> type : superClass.getJavaClasses()) {
          if (!type.isAssignableFrom(javaClass)) {
            classes.add(type);
          }
        }
      }
      this.javaClasses = classes.toArray(new Class<?>[classes.size()]);
    }
    this.virtual = virtual;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Class<? extends VirtualBean> getJavaClass() {

    return (Class<? extends VirtualBean>) super.getJavaClass();
  }

  @Override
  public Class<?>[] getJavaClasses() {

    return this.javaClasses;
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

  @SuppressWarnings("exports")
  @Override
  public BeanProperties create(AbstractBean bean) {

    boolean threadSafe = BeanAccessor.isThreadSafe(bean);
    return new BeanPropertiesMap(threadSafe);
  }

  /**
   * @param javaClass the {@link Class} reflecting the {@link AdvancedBean}.
   * @return the {@link BeanClass} for this instance.
   */
  public static BeanClassImpl asClass(Class<? extends VirtualBean> javaClass) {

    return asClass(javaClass, BeanClassImpl::getBeanClass);
  }

  /**
   * @param key the fully-qualified (virtual or physical) class-name.
   * @return the {@link BeanClass} for this instance.
   */
  public static BeanClassImpl getClass(String key) {

    return CLASS_MAP.get(key);
  }

  /**
   * @param javaClass the {@link Class} reflecting the {@link AdvancedBean}.
   * @param factory a {@link Function} to get or create {@link BeanClassImpl} by {@link Class}.
   * @return the {@link BeanClass} for this instance.
   */
  public static BeanClassImpl asClass(Class<? extends VirtualBean> javaClass,
      Function<Class<?>, BeanClassImpl> factory) {

    String key = javaClass.getName();
    return CLASS_MAP.computeIfAbsent(key, (x) -> createBeanClass(javaClass, factory));
  }

  private static BeanClassImpl createBeanClass(Class<? extends VirtualBean> javaClass,
      Function<Class<?>, BeanClassImpl> factory) {

    List<BeanClassImpl> superClassList = Collections.emptyList();
    if (javaClass.isInterface()) {
      if (javaClass != VirtualBean.class) {
        Class<?>[] interfaces = javaClass.getInterfaces();
        superClassList = new ArrayList<>(interfaces.length);
        for (Class<?> superclass : interfaces) {
          if (VirtualBean.class.isAssignableFrom(superclass)) {
            BeanClassImpl superBeanClass = factory.apply(superclass);
            Objects.requireNonNull(superBeanClass, "factory.apply");
            superClassList.add(superBeanClass);
          }
        }
      }
    } else {
      if (javaClass != AdvancedBean.class) {
        Class<?> superclass = javaClass.getSuperclass();
        if (AdvancedBean.class.isAssignableFrom(superclass)) {
          superClassList = Collections.singletonList(factory.apply(superclass));
        }
      }
    }
    BeanClassImpl beanClass = new BeanClassImpl(javaClass, superClassList);
    return beanClass;
  }

  /**
   * @param javaClass the {@link Class} reflecting the {@link AdvancedBean}.
   * @return the corresponding {@link BeanClass}.
   */
  private static BeanClassImpl getBeanClass(Class<?> javaClass) {

    BeanClassImpl beanClass = CLASS_MAP.get(javaClass.getName());
    if (beanClass == null) {
      throw new IllegalStateException(
          "Super-class " + javaClass + " not registered! It seems you forgot to declare a static prototype instance.");
    }
    return beanClass;
  }

}
