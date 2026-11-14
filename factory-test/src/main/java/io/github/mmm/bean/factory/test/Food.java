package io.github.mmm.bean.factory.test;

import io.github.mmm.bean.BeanFactory;

/// A liquid [Comestible] that can be drunken.
public interface Food extends Comestible {

  /// @return a new instance of [Food].
  static Food of() {

    return BeanFactory.get().create(Food.class);
  }

}
