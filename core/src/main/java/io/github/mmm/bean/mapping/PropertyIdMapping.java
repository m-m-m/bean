package io.github.mmm.bean.mapping;

import io.github.mmm.property.ReadableProperty;

/// Interface for the bidirectional mapping from [name][ReadableProperty#getName()] to numeric IDs and vice versa.
///
/// @see PropertyIdMapper
/// @since 1.0.0
public interface PropertyIdMapping {

  /// @param id the ID of the property.
  /// @return the [name][ReadableProperty#getName()] of the [property][ReadableProperty] with the given ID or
  ///         `null` if no mapping is defined for the given ID.
  String name(int id);

  /// @param property the [ReadableProperty].
  /// @return the ID of the property or `-1` if no mapping is defined for the given ID.
  int id(ReadableProperty<?> property);
}