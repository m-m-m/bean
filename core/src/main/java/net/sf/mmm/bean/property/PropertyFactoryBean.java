/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.property;

import net.sf.mmm.bean.WritableBean;
import net.sf.mmm.property.PropertyMetadata;
import net.sf.mmm.property.ReadableProperty;
import net.sf.mmm.property.WritableProperty;
import net.sf.mmm.property.factory.AbstractPropertyFactory;
import net.sf.mmm.property.factory.PropertyFactory;

/**
 * Implementation of {@link PropertyFactory} for {@link BeanProperty}.
 *
 * @param <V> type of the {@link WritableBean bean}.
 * @since 1.0.0
 */
public class PropertyFactoryBean<V extends WritableBean> extends AbstractPropertyFactory<V, BeanProperty<V>> {

  @SuppressWarnings({ "unchecked", "rawtypes" })
  @Override
  public Class<? extends V> getValueClass() {

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
  public BeanProperty<V> create(String name, Class<? extends V> valueClass, PropertyMetadata<V> metadata) {

    return new BeanProperty<>(name, (Class) valueClass, metadata);
  }

  @Override
  public boolean isPolymorphic() {

    return true;
  }

}
