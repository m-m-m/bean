package io.github.mmm.bean.impl.properties;

/// Interface for the names of the properties of a static bean. Have to be managed in alphabetical order.
public interface BeanPropertyNames {

  /// @param name the [property name][io.github.mmm.property.WritableProperty#getName()].
  /// @return the index of the [property][io.github.mmm.property.WritableProperty] or a negative value if not found.
  int indexOf(String name);

  /// @return the total number of properties.
  int size();

}
