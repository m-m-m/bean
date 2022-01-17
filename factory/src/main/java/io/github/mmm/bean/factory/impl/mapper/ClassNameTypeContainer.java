package io.github.mmm.bean.factory.impl.mapper;

import io.github.mmm.bean.impl.BeanTypeImpl;
import io.github.mmm.bean.mapping.ClassNameMapper.ClassType;

/**
 * Container for {@link Class}, {@link io.github.mmm.bean.mapping.ClassNameMapper#getName(Class) name}, and
 * {@link ClassType}.
 *
 * @since 1.0.0
 */
public class ClassNameTypeContainer {

  final Class<?> javaClass;

  final String name;

  final ClassType classType;

  /**
   * The constructor.
   *
   * @param javaClass the java {@link Class}.
   * @param classType the {@link ClassType}.
   */
  public ClassNameTypeContainer(Class<?> javaClass, ClassType classType) {

    this(javaClass, classType, null);
  }

  /**
   * The constructor.
   *
   * @param javaClass the java {@link Class}.
   * @param classType the {@link ClassType}.
   * @param name the {@link io.github.mmm.bean.mapping.ClassNameMapper#getName(Class) class name}.
   */
  public ClassNameTypeContainer(Class<?> javaClass, ClassType classType, String name) {

    super();
    this.javaClass = javaClass;
    if (name == null) {
      this.name = computeName(javaClass, classType);
    } else {
      this.name = name;
    }
    this.classType = classType;
  }

  private static String computeName(Class<?> javaClass, ClassType classType) {

    if (classType == ClassType.BEAN) {
      return BeanTypeImpl.getStableName(javaClass, null);
    }
    String name = javaClass.getName();
    if (name.startsWith("java.") || name.startsWith("io.github.mmm.")) {
      name = javaClass.getSimpleName();
    }
    return name;
  }

}
