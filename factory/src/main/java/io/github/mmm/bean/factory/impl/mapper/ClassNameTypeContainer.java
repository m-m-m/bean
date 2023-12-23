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

  final String qualifiedName;

  final String simpleName;

  final String name;

  final ClassType classType;

  final int priority;

  /**
   * The constructor.
   *
   * @param javaClass the java {@link Class}.
   * @param classType the {@link ClassType}.
   */
  public ClassNameTypeContainer(Class<?> javaClass, ClassType classType) {

    super();
    this.javaClass = javaClass;
    this.classType = classType;
    this.simpleName = javaClass.getSimpleName();
    if (classType == ClassType.BEAN) {
      this.qualifiedName = BeanTypeImpl.getStableName(javaClass);
    } else {
      this.qualifiedName = javaClass.getName();
    }
    if (this.qualifiedName.startsWith("java.")) {
      this.priority = 3;
      this.name = this.simpleName;
    } else if (this.qualifiedName.startsWith("io.github.mmm.")) {
      this.priority = 2;
      this.name = this.simpleName;
    } else {
      this.priority = 1;
      this.name = this.qualifiedName;
    }
  }

}
