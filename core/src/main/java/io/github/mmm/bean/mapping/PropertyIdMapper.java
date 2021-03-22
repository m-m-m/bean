package io.github.mmm.bean.mapping;

import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.impl.mapping.PropertyIdMapperImpl;
import io.github.mmm.bean.impl.mapping.PropertyIdMappingMap;

/**
 * Interface to {@link #getIdMapping(ReadableBean) retrieve} the {@link PropertyIdMapping} for a given
 * {@link ReadableBean}.
 *
 * @since 1.0.0
 */
public interface PropertyIdMapper {

  /**
   * @param bean the {@link ReadableBean} to get the mapping for.
   * @return the {@link PropertyIdMappingMap} for the given {@link ReadableBean bean}.
   */
  PropertyIdMapping getIdMapping(ReadableBean bean);

  /**
   * @return the singleton instance of this {@link PropertyIdMapper}.
   */
  static PropertyIdMapper get() {

    return PropertyIdMapperImpl.INSTANCE;
  }

}
