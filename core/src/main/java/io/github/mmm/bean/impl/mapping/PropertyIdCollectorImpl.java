package io.github.mmm.bean.impl.mapping;

import java.util.HashMap;
import java.util.Map;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.bean.mapping.PropertyIdCollector;
import io.github.mmm.bean.mapping.PropertyIdMapping;
import io.github.mmm.property.ReadableProperty;

/**
 * Implementation of {@link PropertyIdCollector}.
 *
 * @since 1.0.0
 */
public class PropertyIdCollectorImpl implements PropertyIdCollector {

  private int counter;

  private String[] id2nameArray;

  private Map<Integer, String> id2nameMap;

  /**
   * The constructor.
   */
  public PropertyIdCollectorImpl() {

    this(16);
  }

  /**
   * The constructor.
   *
   * @param capacity the capacity of the array used to avoid {@link Map} allocation.
   */
  public PropertyIdCollectorImpl(int capacity) {

    super();
    this.id2nameArray = new String[capacity];
  }

  @Override
  public void add(ReadableProperty<?> property, int id) {

    if (id <= 0) {
      throw new IllegalArgumentException(Integer.toString(id));
    }
    Integer idObject = Integer.valueOf(id);
    String name = property.getName();
    if ((this.id2nameMap == null) && (id <= this.id2nameArray.length)) {
      if (this.id2nameArray[id - 1] != null) {
        throw new DuplicateObjectException("property.id", idObject, this.id2nameArray[id - 1]);
      }
      this.id2nameArray[id - 1] = name;
    } else {
      if (this.id2nameMap == null) {
        this.id2nameMap = new HashMap<>(this.id2nameArray.length + 16);
        for (int i = 0; i < this.id2nameArray.length; i++) {
          if (this.id2nameArray[i] != null) {
            this.id2nameMap.put(Integer.valueOf(i + 1), this.id2nameArray[i]);
          }
        }
      }
      String duplicate = this.id2nameMap.put(idObject, name);
      if (duplicate != null) {
        throw new DuplicateObjectException("property.id", idObject, this.id2nameArray[id - 1]);
      }
    }
    if ((id > this.counter) && ((id - this.counter) < 128)) {
      this.counter = id;
    }
  }

  @Override
  public void add(ReadableProperty<?> property) {

    this.counter++;
    add(property, this.counter);
  }

  @Override
  public void addOffset(int offset) {

    this.counter = this.counter + offset;
  }

  /**
   * @return the {@link PropertyIdMapping} for the collected ID assignments.
   */
  public PropertyIdMapping build() {

    if (this.id2nameMap == null) {
      return new PropertyIdMappingSimple(this.id2nameArray);
    }
    return new PropertyIdMappingMap(this.id2nameMap);
  }

}
