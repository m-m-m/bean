package io.github.mmm.bean.impl.mapping;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.github.mmm.bean.BeanType;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.mapping.PropertyIdMapper;
import io.github.mmm.bean.mapping.PropertyIdMapping;

/// Implementation of [PropertyIdMapper].
///
/// @since 1.0.0
public class PropertyIdMapperImpl implements PropertyIdMapper {

  /// The singleton instance.
  public static final PropertyIdMapperImpl INSTANCE = new PropertyIdMapperImpl();

  private final Map<BeanType, PropertyIdMapping> bean2idMappingMap;

  /// The constructor.
  public PropertyIdMapperImpl() {

    super();
    this.bean2idMappingMap = new ConcurrentHashMap<>();
  }

  /// @param bean the [ReadableBean] to get the mapping for.
  /// @return the [PropertyIdMappingMap] for the given [bean][ReadableBean].
  @Override
  public PropertyIdMapping getIdMapping(ReadableBean bean) {

    BeanType type = bean.getType();
    return this.bean2idMappingMap.computeIfAbsent(type, _ -> createMapping(bean));
  }

  private PropertyIdMapping createMapping(ReadableBean bean) {

    PropertyIdCollectorImpl collector = new PropertyIdCollectorImpl();
    bean.mapPropertyIds(collector);
    return collector.build();
  }

}
