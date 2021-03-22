package io.github.mmm.bean.mapping;

import io.github.mmm.property.ReadableProperty;

/**
 * Interface for the bidirectional mapping from {@link ReadableProperty#getName() name} to numeric IDs and vice versa.
 *
 * @see PropertyIdMapper
 * @since 1.0.0
 */
public interface PropertyIdMapping {

  /**
   * @param id the ID of the property.
   * @return the {@link ReadableProperty#getName() name} of the {@link ReadableProperty property} with the given ID or
   *         {@code null} if no mapping is defined for the given ID.
   */
  String name(int id);

  /**
   * @param property the {@link ReadableProperty}.
   * @return the ID of the property or {@code -1} if no mapping is defined for the given ID.
   */
  int id(ReadableProperty<?> property);
}