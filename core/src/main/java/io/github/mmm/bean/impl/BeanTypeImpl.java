/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.impl;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import io.github.mmm.bean.BeanType;
import io.github.mmm.bean.Name;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.WritableBean;

/**
 * Implementation of {@link BeanType}.
 *
 * @see ReadableBean#getType()
 */
public class BeanTypeImpl implements BeanType {

  private static final Map<String, BeanTypeImpl> CLASS_MAP = new ConcurrentHashMap<>();

  private final Class<? extends WritableBean> javaClass;

  private final String stableName;

  /**
   * The constructor.
   *
   * @param javaClass the {@link #getJavaClass() java class}.
   * @param stableName the {@link #getStableName() stable name}.
   */
  protected BeanTypeImpl(Class<? extends WritableBean> javaClass, String stableName) {

    super();
    Objects.requireNonNull(javaClass, "javaClass");
    this.javaClass = javaClass;
    this.stableName = getStableName(javaClass, stableName);
  }

  /**
   * The constructor.
   *
   * @param template the template to copy.
   */
  protected BeanTypeImpl(BeanTypeImpl template) {

    super();
    this.javaClass = template.javaClass;
    this.stableName = template.stableName;
  }

  /**
   * @param javaClass the {@link Class} reflecting the {@link WritableBean}.
   * @param stableName the {@link #getStableName() stable name} or {@code null} to determine automatically.
   * @return the {@link #getStableName() stable name}.
   */
  public static String getStableName(Class<? extends WritableBean> javaClass, String stableName) {

    if (stableName != null) {
      return stableName;
    }
    Name name = javaClass.getAnnotation(Name.class);
    if (name == null) {
      return javaClass.getSimpleName();
    } else {
      return name.value();
    }
  }

  @Override
  public Class<? extends WritableBean> getJavaClass() {

    return this.javaClass;
  }

  @Override
  public String getPackageName() {

    return this.javaClass.getPackageName();
  }

  @Override
  public String getSimpleName() {

    return this.javaClass.getSimpleName();
  }

  @Override
  public String getStableName() {

    return this.stableName;
  }

  @Override
  public String getQualifiedName() {

    return this.javaClass.getName();
  }

  @Override
  public boolean isVirtual() {

    return false;
  }

  @Override
  public int hashCode() {

    return this.javaClass.hashCode();
  }

  @Override
  public boolean equals(Object obj) {

    if (obj == this) {
      return true;
    } else if ((obj == null) || !(obj instanceof BeanTypeImpl)) {
      return false;
    }
    BeanTypeImpl other = (BeanTypeImpl) obj;
    return (this.javaClass.equals(other.javaClass)) //
        && (this.stableName.equals(other.stableName));
  }

  @Override
  public String toString() {

    return getQualifiedName();
  }

  /**
   * @param javaClass the {@link #getJavaClass() java class}.
   * @return the {@link BeanType} representing the given {@link Class}.
   */
  public static BeanTypeImpl of(Class<? extends WritableBean> javaClass) {

    return of(javaClass, null);
  }

  /**
   * @param javaClass the {@link #getJavaClass() java class}.
   * @param stableName the custom {@link #getStableName() stable name}.
   * @return the {@link BeanType} representing the given {@link Class}.
   */
  public static BeanTypeImpl of(Class<? extends WritableBean> javaClass, String stableName) {

    if (javaClass == null) {
      return null;
    }
    return CLASS_MAP.computeIfAbsent(javaClass.getName(), (x) -> new BeanTypeImpl(javaClass, stableName));
  }
}
