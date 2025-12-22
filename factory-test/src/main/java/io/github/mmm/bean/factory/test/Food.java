package io.github.mmm.bean.factory.test;

import io.github.mmm.bean.BeanFactory;

/**
 * A liquid {@link Comestible} that can be drunken.
 */
public interface Food extends Comestible {

  static Food of() {

    return BeanFactory.get().create(Food.class);
  }

}
