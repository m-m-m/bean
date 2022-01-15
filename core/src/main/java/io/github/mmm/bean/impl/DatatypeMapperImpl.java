package io.github.mmm.bean.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.bean.mapping.DatatypeMapper;
import io.github.mmm.property.factory.PropertyFactoryManager;

/**
 * Class to map from Java {@link Class} to their {@link Class#getName()} or {@link Class#getSimpleName() simple name}
 * and vice-versa in a secure way (without {@link ClassLoader} injection).
 *
 */
public class DatatypeMapperImpl implements DatatypeMapper {

  /** The singleton instance. */
  public static final DatatypeMapperImpl INSTANCE = new DatatypeMapperImpl();

  private static final Logger LOG = LoggerFactory.getLogger(DatatypeMapperImpl.class);

  private Map<String, Class<?>> name2typeMap;

  private Map<Class<?>, String> type2nameMap;

  /**
   * The constructor.
   */
  public DatatypeMapperImpl() {

    super();
    this.name2typeMap = new HashMap<>();
    this.type2nameMap = new HashMap<>();
    addDefaults();
  }

  /**
   *
   */
  protected void addDefaults() {

    for (Class<?> type : PropertyFactoryManager.get().getValueTypes()) {
      add(type);
    }
  }

  /**
   * @param type the datatype to register.
   */
  protected void add(Class<?> type) {

    add(type, computeName(type));
  }

  /**
   * @param type the datatype to register.
   * @param name the name for the datatype to register.
   */
  protected void add(Class<?> type, String name) {

    String duplicateName = this.type2nameMap.put(type, name);
    if (duplicateName != null) {
      throw new DuplicateObjectException(type.getName(), name, duplicateName);
    }
    Class<?> duplicateClass = this.name2typeMap.put(name, type);
    if (duplicateClass != null) {
      throw new DuplicateObjectException(name, type.getName(), duplicateClass.getName());
    }
  }

  @Override
  public String getName(Class<?> type) {

    String name = this.type2nameMap.get(type);
    if (name == null) {
      LOG.warn("Unknown datatype {}", type);
      name = computeName(type);
    }
    return name;
  }

  private String computeName(Class<?> type) {

    String name = type.getName();
    if (name.startsWith("java.") || name.startsWith("io.github.mmm.")) {
      name = type.getSimpleName();
    }
    return name;
  }

  @Override
  public Class<?> getType(String name) {

    return this.name2typeMap.get(name);
  }

}
