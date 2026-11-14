/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.property;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.value.observable.object.WritableObjectValue;

/// [WritableProperty] with [WritableBean] [value][#getValue()].
///
/// @param <V> type of the [bean][WritableBean] [value][#getValue()].
/// @since 1.0.0
public interface WritableBeanProperty<V extends WritableBean>
    extends ReadableBeanProperty<V>, WritableProperty<V>, WritableObjectValue<V> {

}
