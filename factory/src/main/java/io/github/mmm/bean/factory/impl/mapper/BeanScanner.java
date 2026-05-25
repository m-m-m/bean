package io.github.mmm.bean.factory.impl.mapper;

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.resource.ModuleAccess;
import io.github.mmm.base.resource.ResourceMap;
import io.github.mmm.base.resource.ResourceType;
import io.github.mmm.base.resource.ResourceTypeScannerService;
import io.github.mmm.base.type.JavaType;
import io.github.mmm.base.type.JavaTypeKind;
import io.github.mmm.bean.VirtualBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.mapping.ClassNameMapper;

/**
 * Implementation of {@link ClassNameMapper}.
 *
 * @since 1.0.0
 */
public class BeanScanner implements ResourceTypeScannerService {

  private static final Logger LOG = LoggerFactory.getLogger(BeanScanner.class);

  private static final Set<String> BEAN_INTERFACE_NAMES = Set.of(WritableBean.class.getName(),
      VirtualBean.class.getName());

  private final Set<String> inclusions;

  private final Set<String> exclusions;

  /**
   * The constructor.
   */
  public BeanScanner() {

    super();
    this.inclusions = new HashSet<>(BEAN_INTERFACE_NAMES);
    this.exclusions = new HashSet<>();
  }

  @Override
  public boolean includeModule(ModuleAccess module) {

    if (module.isInternalModule()) {
      return false;
    }
    String name = module.getResolved().name();
    if (name.startsWith("ch.qos") || name.startsWith("org.slf4j")) {
      return false;
    }
    this.exclusions.clear();
    return true;
  }

  @Override
  public boolean scanType(ResourceType type, ResourceMap resources) {

    if (type.isInnerType() || type.isPackageInfo() || type.isModuleInfo() || !type.getParent().isOpen()) {
      return false; // fast exclusion
    }
    LOG.trace("Checking if type {} is bean...", type);
    String name = type.getName();
    // already visited recursively?
    if (this.inclusions.contains(name)) {
      return true;
    } else if (this.exclusions.contains(name)) {
      return false;
    }
    // fast pre-load of class-file
    JavaType javaType = type.loadType();
    if (javaType.isBroken()) {
      this.exclusions.add(name);
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
          include = scanParent(resources, interfaceName);
          if (include) {
            break;
          }
        }
      } else if (kind.isClass()) {
        String superClass = javaType.getSuperClass();
        include = scanParent(resources, superClass);
      }
    }
    if (include) {
      this.inclusions.add(name);
      LOG.debug("Found bean {}", name);
    } else {
      this.exclusions.add(name); // we ignore all sub-types
    }
    return include;
  }

  private boolean scanParent(ResourceMap resources, String superTypeName) {

    ResourceType superType = resources.getTypeByName(superTypeName);
    if (superType == null) {
      // if superType is null, it is from a different module that has been scanned first.
      return this.inclusions.contains(superTypeName);
    } else {
      return scanType(superType, resources);
    }
  }

}
