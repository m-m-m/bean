/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.generator;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import io.github.mmm.bean.VirtualBean;
import io.github.mmm.bean.WritableBean;

/**
 *
 */
public class BeanScanner implements AutoCloseable {

  private static final Set<String> BEAN_INTERFACE_NAMES = Set.of(WritableBean.class.getName(),
      VirtualBean.class.getName());

  private final ClassLoader classloader;

  private ScanResult scanResult;

  /**
   * The constructor.
   *
   * @param classloader the optional {@link ClassLoader} to use.
   */
  public BeanScanner(ClassLoader classloader) {

    super();
    ClassGraph classGraph = new ClassGraph();
    if (classloader == null) {
      this.classloader = Thread.currentThread().getContextClassLoader();
    } else {
      this.classloader = classloader;
      classGraph.addClassLoader(classloader);
    }
    this.scanResult = classGraph.enableClassInfo().scan();
  }

  /**
   * @return the {@link Collection} of {@link Class}es reflecting the non-abstract bean interfaces.
   */
  public Collection<Class<? extends WritableBean>> findBeanInterfaces() {

    Collection<Class<? extends WritableBean>> result = new ArrayList<>();
    for (ClassInfo classInfo : this.scanResult.getAllInterfaces()) {
      // if (!classInfo.hasAnnotation(AbstractInterface.class.getName())) {
      if (isBeanInterface(classInfo)) {
        Class<? extends WritableBean> beanClass = loadClass(classInfo);
        result.add(beanClass);
      }
      // }
    }
    return result;
  }

  private boolean isBeanInterface(ClassInfo classInfo) {

    String name = classInfo.getName();
    if (BEAN_INTERFACE_NAMES.contains(name)) {
      return true;
    }
    ClassInfoList interfaces = classInfo.getInterfaces();
    for (ClassInfo interfaceInfo : interfaces) {
      if (isBeanInterface(interfaceInfo)) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return the {@link Collection} of {@link Class}es reflecting the non-abstract bean classes.
   */
  public Collection<Class<? extends WritableBean>> findBeanClasses() {

    Collection<Class<? extends WritableBean>> result = new ArrayList<>();
    for (ClassInfo classInfo : this.scanResult.getClassesImplementing(WritableBean.class.getName())) {
      if (!classInfo.isAbstract()) {
        Class<? extends WritableBean> beanClass = loadClass(classInfo);
        result.add(beanClass);
      }
    }
    return result;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  public static Collection<Class<? extends WritableBean>> findBeans(ClassLoader classloader) {

    Collection<Class<?>> result = new ArrayList<>();
    try (ScanResult scanResult = new ClassGraph().enableAllInfo() // Scan classes, methods, fields, annotations
        .scan()) { // Start the scan
      for (ClassInfo classInfo : scanResult.getAllInterfaces()) {

        System.out.println(classInfo.getName());
      }
    }
    return (Collection) result;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private <T> Class<T> loadClass(ClassInfo classInfo) {

    try {
      Class<?> type = this.classloader.loadClass(classInfo.getName());
      return (Class) type;
    } catch (Exception e) {
      throw new IllegalStateException(e);
    }
  }

  @Override
  public void close() {

    if (this.scanResult != null) {
      this.scanResult.close();
      this.scanResult = null;
    }
  }

}
