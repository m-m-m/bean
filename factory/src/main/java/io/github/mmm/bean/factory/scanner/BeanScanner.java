/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.scanner;

import java.lang.module.ModuleDescriptor;
import java.lang.module.ModuleDescriptor.Exports;
import java.lang.module.ModuleDescriptor.Opens;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ModuleInfo;
import io.github.classgraph.ModuleRef;
import io.github.classgraph.ScanResult;
import io.github.mmm.bean.AbstractInterface;
import io.github.mmm.bean.VirtualBean;
import io.github.mmm.bean.WritableBean;

/**
 * Scans the class-/module-path for {@link WritableBean bean} classes and interfaces.
 *
 * @since 1.0.0
 */
public class BeanScanner implements AutoCloseable {

  private static final Logger LOG = LoggerFactory.getLogger(BeanScanner.class);

  private static final Set<String> BEAN_INTERFACE_NAMES = Set.of(WritableBean.class.getName(),
      VirtualBean.class.getName());

  private final ClassLoader classloader;

  private ScanResult scanResult;

  private ModuleExports moduleExports;

  /**
   * The constructor.
   */
  public BeanScanner() {

    this(null);
  }

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
    this.scanResult = classGraph.enableClassInfo().enableAnnotationInfo().scan();
    this.moduleExports = new ModuleExports();
  }

  /**
   * @return the {@link Collection} of {@link Class}es reflecting the non-abstract bean interfaces.
   */
  public Collection<Class<? extends WritableBean>> findBeanInterfaces() {

    Collection<Class<? extends WritableBean>> result = new ArrayList<>();
    for (ClassInfo classInfo : this.scanResult.getAllInterfaces()) {
      if (classInfo.isPublic() && !classInfo.hasAnnotation(AbstractInterface.class) && isBeanInterface(classInfo)) {
        Boolean exported = this.moduleExports.isExported(classInfo);
        if (!Boolean.FALSE.equals(exported)) {
          Class<? extends WritableBean> beanClass = loadClass(classInfo);
          result.add(beanClass);
        }
      }
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
        loadBeanClass(classInfo, result);
      }
    }
    return result;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  private <T> Class<T> loadClass(ClassInfo classInfo) {

    String name = classInfo.getName();
    try {
      Class<?> type = this.classloader.loadClass(name);
      return (Class) type;
    } catch (Exception e) {
      LOG.warn("Failed to load bean type {}", name, e);
      return null;
    }
  }

  private void loadBeanClass(ClassInfo classInfo, Collection<Class<? extends WritableBean>> beanClasses) {

    Boolean exported = this.moduleExports.isExported(classInfo);
    boolean classAdded = false;
    if (!Boolean.FALSE.equals(exported)) {
      Class<? extends WritableBean> beanClass = loadClass(classInfo);
      if ((beanClass != null) && (exported != null) && this.moduleExports.isExported(beanClass)) {
        beanClasses.add(beanClass);
        classAdded = true;
      }
    }
    if (!classAdded) {
      LOG.debug("Omitting bean type {} that is not visible.", classInfo.getName());
    }
  }

  @Override
  public void close() {

    if (this.scanResult != null) {
      this.scanResult.close();
      this.scanResult = null;
    }
  }

  private static class ModuleExports {

    private final Map<String, Set<String>> moduleExports;

    private ModuleExports() {

      super();
      this.moduleExports = new HashMap<>();
    }

    Boolean isExported(ClassInfo classInfo) {

      ModuleInfo moduleInfo = classInfo.getModuleInfo();
      if (moduleInfo == null) {
        return null;
      }
      ModuleRef moduleRef = moduleInfo.getModuleRef();
      if (moduleRef == null) {
        return null;
      }
      Object descriptor = moduleRef.getDescriptor();
      if (descriptor instanceof ModuleDescriptor) {
        ModuleDescriptor moduleDescriptor = (ModuleDescriptor) descriptor;
        String pkg = classInfo.getPackageName();
        return Boolean.valueOf(isExported(moduleDescriptor, pkg));
      }
      return null;
    }

    boolean isExported(Class<?> type) {

      Module module = type.getModule();
      if (module == null) {
        return true;
      }
      ModuleDescriptor moduleDescriptor = module.getDescriptor();
      if (moduleDescriptor == null) {
        return true;
      }
      return isExported(moduleDescriptor, type.getPackageName());
    }

    private boolean isExported(ModuleDescriptor module, String pkg) {

      if (module.isOpen()) {
        return true;
      }
      if (module.isAutomatic()) {
        // https://docs.oracle.com/javase/9/docs/api/java/lang/module/ModuleDescriptor.html
        return true;
      }
      Set<String> exports = getExportedPackages(module);
      return exports.contains(pkg);
    }

    /**
     * @param module the {@link ModuleDescriptor}.
     * @return the {@link Set} with the package names exported by the given module.
     */
    private Set<String> getExportedPackages(ModuleDescriptor module) {

      String name = module.name();
      return this.moduleExports.computeIfAbsent(name, n -> createExportedPackages(module));
    }

    private Set<String> createExportedPackages(ModuleDescriptor module) {

      Set<String> packages = new HashSet<>();
      for (Exports export : module.exports()) {
        packages.add(export.source());
      }
      for (Opens opens : module.opens()) {
        packages.add(opens.source());
      }
      return packages;
    }

  }

}
