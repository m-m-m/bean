/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.entity;

import net.sf.mmm.bean.old.MagicBean;
import net.sf.mmm.property.WritableProperty;
import net.sf.mmm.util.data.api.entity.Entity;
import net.sf.mmm.util.data.api.id.Id;

/**
 * This is the interface for an {@link Entity} as a {@link MagicBean}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface EntityBean extends MagicBean, Entity {

  /**
   * @return the {@link WritableProperty property} containing the {@link #getId() primary key}.
   */
  WritableProperty<Id<?>> Id();

}
