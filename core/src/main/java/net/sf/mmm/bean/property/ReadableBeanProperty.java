/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.property;

import net.sf.mmm.bean.Bean;
import net.sf.mmm.property.ReadableProperty;
import net.sf.mmm.property.object.ReadableObjectProperty;

/**
 * {@link ReadableProperty} with {@link Boolean} {@link #getValue() value}.
 *
 * @since 1.0.0
 */
public interface ReadableBeanProperty extends ReadableObjectProperty<Bean> {

  @Override
  default Class<Bean> getValueClass() {

    return Bean.class;
  }

}
