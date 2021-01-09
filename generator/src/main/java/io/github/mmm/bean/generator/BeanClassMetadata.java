/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.generator;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;
import java.lang.reflect.Parameter;

import io.github.mmm.bean.BeanClass;
import io.github.mmm.bean.WritableBean;

/**
 *
 */
public class BeanClassMetadata extends BeanMetadata {

  private final boolean hasBeanClassConstructor;

  /**
   * The constructor.
   *
   * @param beanClass the {@link #getBeanType() bean type}.
   */
  private BeanClassMetadata(Class<? extends WritableBean> beanClass, boolean hasBeanClassConstructor) {

    super(beanClass);
    this.hasBeanClassConstructor = hasBeanClassConstructor;
  }

  @Override
  public void writeInstantiation(Writer writer, String beanClassVariable) throws IOException {

    writer.write("new ");
    writer.write(this.beanType.getName());
    writer.write("(");
    if (this.hasBeanClassConstructor) {
      writer.write(beanClassVariable);
    }
    writer.write(")");
  }

  /**
   * @param type the {@link #getBeanType() bean type} to check.
   * @return {@code true} if the given {@link Class} reflects a Java class (not an interface, enum, annoataion, etc.)
   *         and is not {@link Modifier#isAbstract(int) abstract}, {@code false} otherwise.
   */
  public static boolean isNonAbstractClass(Class<?> type) {

    if (type.isInterface()) {
      return false;
    } else if (type.isEnum()) {
      return false;
    } else if (type.isAnnotation()) {
      return false;
    } else if (Modifier.isAbstract(type.getModifiers())) {
      return false;
    }
    return true;
  }

  /**
   * @param beanClass the {@link Class} reflecting the {@link WritableBean} to get metadata for.
   * @return the {@link BeanInterfaceMetadata} for the given {@link Class} or {@code null} if not a
   *         {@link #isNonAbstractClass(Class) non-abstract class} or not having public non-arg or {@link BeanClass}
   *         constructor.
   */
  public static BeanClassMetadata of(Class<? extends WritableBean> beanClass) {

    if (isNonAbstractClass(beanClass)) {
      Constructor<?> nonArgConstructor = null;
      Constructor<?> beanClassConstructor = null;
      for (Constructor<?> constructor : beanClass.getConstructors()) {
        Parameter[] parameters = constructor.getParameters();
        if (parameters.length == 0) {
          nonArgConstructor = constructor;
        } else if ((parameters.length == 1) && (BeanClass.class.equals(parameters[0].getType()))) {
          beanClassConstructor = constructor;
        }
      }
      if ((nonArgConstructor != null) || (beanClassConstructor != null)) {
        return new BeanClassMetadata(beanClass, (beanClassConstructor != null));
      }
    }
    return null;
  }

}
