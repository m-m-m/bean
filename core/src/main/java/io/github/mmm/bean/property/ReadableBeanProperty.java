/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.property;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.object.ReadableObjectProperty;

/**
 * {@link ReadableProperty} with {@link WritableBean} {@link #getValue() value}.
 *
 * @param <V> type of the {@link WritableBean bean} {@link #getValue() value}.
 * @since 1.0.0
 */
public interface ReadableBeanProperty<V extends WritableBean> extends ReadableObjectProperty<V> {

  @Override
  default V getFallbackSafeValue() {

    Class<V> valueClass = getValueClass();
    if (valueClass == null) {
      return null;
    }
    return BeanFactory.get().create(valueClass);
  }

}
