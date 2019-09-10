/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.property;

import net.sf.mmm.bean.Bean;
import net.sf.mmm.property.WritableProperty;
import net.sf.mmm.value.observable.object.WritableObjectValue;

/**
 * {@link WritableProperty} with {@link Boolean} {@link #getValue() value}.
 *
 * @since 1.0.0
 */
public interface WritableBeanProperty extends ReadableBeanProperty, WritableProperty<Bean>, WritableObjectValue<Bean> {

}
