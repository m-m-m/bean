package io.github.mmm.bean.impl;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.BeanType;
import io.github.mmm.bean.WritableBean;

/**
 * Gives access to {@link #isThreadSafe(AbstractBean) thread-safe flag}.
 */
public abstract class BeanAccessor extends AbstractBean {

  private BeanAccessor(WritableBean writable) {

    super(writable);
  }

  @Override
  public BeanType getType() {

    return null;
  }

  @Override
  public boolean isPrototype() {

    return false;
  }

  protected static boolean isThreadSafe(AbstractBean bean) {

    return AbstractBean.isThreadSafe(bean);
  }

}
