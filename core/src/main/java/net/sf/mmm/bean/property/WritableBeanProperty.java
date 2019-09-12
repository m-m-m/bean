/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.property;

import net.sf.mmm.bean.WritableBean;
import net.sf.mmm.property.WritableProperty;
import net.sf.mmm.value.observable.object.WritableObjectValue;

/**
 * {@link WritableProperty} with {@link WritableBean} {@link #getValue() value}.
 *
 * @param <V> type of the {@link WritableBean bean} {@link #getValue() value}.
 * @since 1.0.0
 */
public interface WritableBeanProperty<V extends WritableBean>
    extends ReadableBeanProperty<V>, WritableProperty<V>, WritableObjectValue<V> {

}
