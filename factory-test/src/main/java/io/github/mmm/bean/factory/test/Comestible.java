package io.github.mmm.bean.factory.test;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.string.StringProperty;

/**
 * A {@link Comestible} is something to eat or drink.
 */
public abstract interface Comestible extends WritableBean {

  @Override
  default boolean isPolymorphic() {

    return true;
  }

  /** @return {@code true} if liquid, {@code false} otherwise. */
  default boolean isLiquid() {

    return false;
  }

  /** @return the name of this comestible. */
  StringProperty Name();

  static Comestible of() {

    return BeanFactory.get().create(Comestible.class);
  }

}
