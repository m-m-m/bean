package io.github.mmm.bean.impl.properties;

import io.github.mmm.bean.AbstractBean;

/**
 * Interface for the factory used to create a proper {@link BeanProperties} instance.
 */
public interface BeanPropertiesFactory {

  /**
   * @param bean the {@link AbstractBean bean} for which to create the {@link BeanProperties}.
   * @return the new {@link BeanProperties} to use.
   */
  BeanProperties create(AbstractBean bean);

}
