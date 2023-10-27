package io.github.mmm.bean.impl.properties;

/**
 * Interface for the names of the properties of a static bean. Have to be managed in alphabetical order.
 */
public interface BeanPropertyNames {

  /**
   * @param name the {@link io.github.mmm.property.WritableProperty#getName() property name}.
   * @return the index of the {@link io.github.mmm.property.WritableProperty property} or a negative value if not found.
   */
  int indexOf(String name);

  /**
   * @return the total number of properties.
   */
  int size();

}
