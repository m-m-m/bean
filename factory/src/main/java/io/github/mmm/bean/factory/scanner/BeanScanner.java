/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.scanner;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ModuleScanner;
import io.github.mmm.base.resource.ResourceMap;
import io.github.mmm.base.resource.ResourceType;
import io.github.mmm.base.type.JavaType;
import io.github.mmm.base.type.JavaTypeKind;
import io.github.mmm.bean.VirtualBean;
import io.github.mmm.bean.WritableBean;

/**
 * Scans the module-path for {@link WritableBean bean} classes and interfaces.
 *
 * @since 1.0.0
 */
public class BeanScanner {

  private static final Logger LOG = LoggerFactory.getLogger(BeanScanner.class);

  private static final Set<String> BEAN_INTERFACE_NAMES = Set.of(WritableBean.class.getName(),
      VirtualBean.class.getName());

  private Set<Class<?>> beanInterfaces;

  private Set<Class<?>> beanClasses;

  /**
   * The constructor.
   */
  public BeanScanner() {

    super();
    this.beanInterfaces = new HashSet<>(128);
    this.beanClasses = new HashSet<>(128);
    HashSet<String> inclusions = new HashSet<>(BEAN_INTERFACE_NAMES);
    HashSet<String> exclusions = new HashSet<>();
    Collection<ModuleAccess> modules = ModuleScanner.get().getAll();
    for (ModuleAccess moduleAccess : modules) {
      if (!moduleAccess.isInternalModule()) {
        String name = moduleAccess.getResolved().name();
        if (!name.startsWith("ch.qos") && !name.startsWith("org.slf4j")) {
          LOG.debug("Scanning module {} for beans...", name);
          exclusions.clear();
          ResourceMap resources = moduleAccess.findResources();
          resources.getTypes().forEach(type -> scanType(type, resources, inclusions, exclusions));
        }
      }
    }
    this.beanInterfaces = Set.copyOf(this.beanInterfaces);
    this.beanClasses = Set.copyOf(this.beanClasses);
  }

  private boolean scanType(ResourceType type, ResourceMap resources, HashSet<String> inclusions,
      HashSet<String> exclusions) {

    if (type.isInnerType() || type.isPackageInfo() || type.isModuleInfo() || !type.getParent().isOpen()) {
      return false; // fast exclusion
    }
    LOG.trace("Checking if type {} is bean...", type);
    String name = type.getName();
    // already visited recursively?
    if (inclusions.contains(name)) {
      return true;
    } else if (exclusions.contains(name)) {
      return false;
    }
    // fast pre-load of class-file
    JavaType javaType;
    try {
      javaType = type.loadType();
    } catch (Throwable e) {
      LOG.warn("Failed to fast-scan class-file for type {}: {}", type, e.getMessage(), e);
      exclusions.add(name);
      return false;
    }
    // only consider interfaces or classes
    JavaTypeKind kind = javaType.getKind();
    if (!kind.isInterface() && !kind.isClass()) {
      return false;
    }
    boolean include = false;
    if (javaType.isPublic()) {
      if (kind.isInterface()) {
        int interfaceCount = javaType.getInterfaceCount();
        for (int i = 0; i < interfaceCount; i++) {
          String interfaceName = javaType.getInterface(i);
          include = scanParent(resources, inclusions, exclusions, interfaceName);
          if (include) {
            break;
          }
        }
      } else if (kind.isClass()) {
        String superClass = javaType.getSuperClass();
        include = scanParent(resources, inclusions, exclusions, superClass);
      }
    }
    if (include) {
      try {
        Class<?> beanClass = type.loadClass();
        inclusions.add(name);
        if (kind.isInterface()) {
          this.beanInterfaces.add(beanClass);
        } else {
          this.beanClasses.add(beanClass);
        }
        LOG.debug("Found bean {}", name);
      } catch (Throwable e) {
        LOG.warn("Failed to load class for type {}: {}", type, e.getMessage(), e);
      }
    } else {
      exclusions.add(name); // we ignore all sub-types
    }
    return include;
  }

  private boolean scanParent(ResourceMap resources, HashSet<String> inclusions, HashSet<String> exclusions,
      String superTypeName) {

    ResourceType superType = resources.getTypeByName(superTypeName);
    if (superType == null) {
      // if superType is null, it is from a different module that has been scanned first.
      return inclusions.contains(superTypeName);
    } else {
      return scanType(superType, resources, inclusions, exclusions);
    }
  }

  /**
   * @return the {@link Collection} of {@link Class}es reflecting the non-abstract bean interfaces.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Collection<Class<? extends WritableBean>> findBeanInterfaces() {

    return (Collection) this.beanInterfaces;
  }

  /**
   * @return the {@link Collection} of {@link Class}es reflecting the non-abstract bean classes.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public Collection<Class<? extends WritableBean>> findBeanClasses() {

    return (Collection) this.beanClasses;
  }

}
