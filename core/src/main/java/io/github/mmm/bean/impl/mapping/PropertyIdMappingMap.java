package io.github.mmm.bean.impl.mapping;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.bean.mapping.PropertyIdMapping;

/**
 * Implementation of {@link PropertyIdMapping} based on {@link Map}.
 */
public class PropertyIdMappingMap extends AbstractPropertyIdMapping {

  private final Map<String, Integer> name2idMap;

  private final Map<Integer, String> id2nameMap;

  PropertyIdMappingMap(Map<Integer, String> id2nameMap) {

    super();
    this.id2nameMap = id2nameMap;
    this.name2idMap = new HashMap<>();
    for (Entry<Integer, String> entry : id2nameMap.entrySet()) {
      Integer duplicate = this.name2idMap.put(entry.getValue(), entry.getKey());
      if (duplicate != null) {
        throw new DuplicateObjectException("property.name", entry.getValue(), duplicate);
      }
    }
  }

  /**
   * The constructor.
   */
  public PropertyIdMappingMap() {

    super();
    this.name2idMap = new HashMap<>();
    this.id2nameMap = new HashMap<>();
  }

  /**
   * @param id the ID.
   * @param name the property name.
   */
  public void put(int id, String name) {

    Integer i = Integer.valueOf(id);
    this.name2idMap.put(name, i);
    this.id2nameMap.put(i, name);
  }

  @Override
  protected int id(String name) {

    Integer integer = this.name2idMap.get(name);
    if (integer != null) {
      return integer.intValue();
    }
    return -1;
  }

  @Override
  public String name(int id) {

    return this.id2nameMap.get(Integer.valueOf(id));
  }

}
