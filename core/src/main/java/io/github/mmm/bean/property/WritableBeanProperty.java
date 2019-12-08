/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.property;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.value.observable.object.WritableObjectValue;

/**
 * {@link WritableProperty} with {@link WritableBean} {@link #getValue() value}.
 *
 * @param <V> type of the {@link WritableBean bean} {@link #getValue() value}.
 * @since 1.0.0
 */
public interface WritableBeanProperty<V extends WritableBean>
    extends ReadableBeanProperty<V>, WritableProperty<V>, WritableObjectValue<V> {

}
