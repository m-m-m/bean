package io.github.mmm.bean.impl.properties;

import io.github.mmm.bean.AbstractBean;

/// Interface for the factory used to create a proper [BeanProperties] instance.
public interface BeanPropertiesFactory {

  /// @param bean the [bean][AbstractBean] for which to create the [BeanProperties].
  /// @return the new [BeanProperties] to use.
  BeanProperties create(AbstractBean bean);

}
