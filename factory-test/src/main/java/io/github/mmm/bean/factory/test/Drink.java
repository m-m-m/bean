package io.github.mmm.bean.factory.test;

import io.github.mmm.bean.BeanFactory;

/**
 * A liquid {@link Comestible} that can be drunken.
 */
public interface Drink extends Comestible {

  @Override
  default boolean isLiquid() {

    return true;
  }

  /**
   * @return a new instance of {@link Drink}.
   */
  static Drink of() {

    return BeanFactory.get().create(Drink.class);
  }

}
