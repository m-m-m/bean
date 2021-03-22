package io.github.mmm.bean.impl.mapping;

import io.github.mmm.bean.mapping.PropertyIdMapping;

/**
 * Simple implementation of {@link PropertyIdMapping}.
 *
 * @since 1.0.0
 */
public class PropertyIdMappingSimple extends AbstractPropertyIdMapping {

  private final String[] names;

  /**
   * The constructor.
   *
   * @param names the array of property names stored using their ID as index.
   */
  public PropertyIdMappingSimple(String... names) {

    super();
    this.names = names;
  }

  @Override
  protected int id(String name) {

    for (int i = 0; i < this.names.length; i++) {
      if (this.names[i] == name) {
        return i + 1;
      }
    }
    return -1;
  }

  @Override
  public String name(int id) {

    if ((id > 0) && (id <= this.names.length)) {
      return this.names[id - 1];
    }
    return null;
  }

}
