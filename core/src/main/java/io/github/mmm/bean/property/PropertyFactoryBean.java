/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.property;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.PropertyMetadata;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.factory.AbstractPropertyFactory;
import io.github.mmm.property.factory.PropertyFactory;
import io.github.mmm.property.factory.PropertyTypeInfo;

/**
 * Implementation of {@link PropertyFactory} for {@link BeanProperty}.
 *
 * @param <V> type of the {@link WritableBean bean}.
 * @since 1.0.0
 */
public class PropertyFactoryBean<V extends WritableBean> extends AbstractPropertyFactory<V, BeanProperty<V>> {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Class<V> getValueClass() {

    return (Class) WritableBean.class;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Class<? extends ReadableProperty<V>> getReadableInterface() {

    return (Class) ReadableBeanProperty.class;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Class<? extends WritableProperty<V>> getWritableInterface() {

    return (Class) WritableBeanProperty.class;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Class<BeanProperty<V>> getImplementationClass() {

    return (Class) BeanProperty.class;
  }

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public BeanProperty<V> create(String name, PropertyTypeInfo<V> typeInfo, PropertyMetadata<V> metadata) {

    Class valueClass = typeInfo.getValueClass();
    if (valueClass == null) {
      valueClass = typeInfo.getTypeArgumentClass(0);
    }
    return new BeanProperty<>(name, valueClass, metadata);
  }

  @Override
  public boolean isPolymorphic() {

    return true;
  }

}
