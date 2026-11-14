package io.github.mmm.bean.mapping;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.property.ReadableProperty;

/// Interface to [collect the property to ID mapping][ReadableBean#mapPropertyIds(PropertyIdCollector)].
///
/// @see PropertyIdMapping
/// @see PropertyIdMapper
/// @since 1.0.0
public interface PropertyIdCollector {

  /// @param property the [property][ReadableProperty] to map.
  /// @param id the explicit ID of the property to map. Please try to use stable but also smaller numbers. In case of
  ///        ProtoBuf (GRPC) numbers from `1` to `15` will only take one byte. Otherwise, if smaller than
  ///        `2048` it will take two bytes.
  void add(ReadableProperty<?> property, int id);

  /// Adds the property as a dynamic mapping with an ID auto-assigned.
  ///
  /// @param property the [property][ReadableProperty] to map.
  void add(ReadableProperty<?> property);

  /// @param offset the number of IDs to reserve for [auto-assignment][#add(ReadableProperty)]. Intended to avoid ID
  ///        collisions when properties are later added to polymorphic bean type hierarchies.
  void addOffset(int offset);

}