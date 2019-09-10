/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.property;

import net.sf.mmm.bean.Bean;
import net.sf.mmm.property.PropertyMetadata;
import net.sf.mmm.property.ReadableProperty;
import net.sf.mmm.property.WritableProperty;
import net.sf.mmm.property.factory.AbstractPropertyFactory;
import net.sf.mmm.property.factory.PropertyFactory;

/**
 * Implementation of {@link PropertyFactory} for {@link BeanProperty}.
 *
 * @since 1.0.0
 */
public class PropertyFactoryBean extends AbstractPropertyFactory<Bean, BeanProperty> {

  @Override
  public Class<? extends Bean> getValueClass() {

    return Bean.class;
  }

  @Override
  public Class<? extends ReadableProperty<Bean>> getReadableInterface() {

    return ReadableBeanProperty.class;
  }

  @Override
  public Class<? extends WritableProperty<Bean>> getWritableInterface() {

    return WritableBeanProperty.class;
  }

  @Override
  public Class<BeanProperty> getImplementationClass() {

    return BeanProperty.class;
  }

  @Override
  public BeanProperty create(String name, Class<? extends Bean> valueClass, PropertyMetadata<Bean> metadata) {

    return new BeanProperty(name, metadata);
  }

}
