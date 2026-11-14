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

/// [BeanMetadataContainer] for a regular [Class] (no [interface][Class#isInterface()]).
///
/// @since 1.0.0
public class BeanMetadataContainerClass extends BeanMetadataContainer {

  private final boolean hasBeanClassConstructor;

  /// The constructor.
  ///
  /// @param beanClass the [bean type][#getBeanType()].
  private BeanMetadataContainerClass(Class<? extends WritableBean> beanClass, boolean hasBeanClassConstructor) {

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

  /// @param type the [bean type][#getBeanType()] to check.
  /// @return `true` if the given [Class] reflects a Java class (not an interface, enum, annoataion, etc.)
  ///         and is not [abstract][Modifier#isAbstract(int)], `false` otherwise.
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

  /// @param beanClass the [Class] reflecting the [WritableBean] to get metadata for.
  /// @return the [BeanMetadataContainerInterface] for the given [Class] or `null` if not a
  ///         [non-abstract class][#isNonAbstractClass(Class)] or not having public non-arg or [BeanClass]
  ///         constructor.
  public static BeanMetadataContainerClass of(Class<? extends WritableBean> beanClass) {

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
        return new BeanMetadataContainerClass(beanClass, (beanClassConstructor != null));
      }
    }
    return null;
  }

}
