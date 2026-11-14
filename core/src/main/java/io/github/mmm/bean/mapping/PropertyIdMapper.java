package io.github.mmm.bean.mapping;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.impl.mapping.PropertyIdMapperImpl;
import io.github.mmm.bean.impl.mapping.PropertyIdMappingMap;

/// Interface to [retrieve][#getIdMapping(ReadableBean)] the [PropertyIdMapping] for a given
/// [ReadableBean].
///
/// @since 1.0.0
public interface PropertyIdMapper {

  /// @param bean the [ReadableBean] to get the mapping for.
  /// @return the [PropertyIdMappingMap] for the given [bean][ReadableBean].
  PropertyIdMapping getIdMapping(ReadableBean bean);

  /// @return the singleton instance of this [PropertyIdMapper].
  static PropertyIdMapper get() {

    return PropertyIdMapperImpl.INSTANCE;
  }

}
