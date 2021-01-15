/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicLong;

import io.github.mmm.bean.impl.BeanClassImpl;
import io.github.mmm.property.WritableProperty;

/**
 * Implementation of {@link VirtualBean} as regular java class. Extend your beans from this class if you need virtual
 * and dynamic typing.
 */
public abstract class AbstractVirtualBean extends AbstractBean implements VirtualBean {

  private static final AtomicLong MODIFICATION_SEQUNCE = new AtomicLong(1);

  private long modificationCounter;

  private long updateCounter;

  private /* final */ BeanClassImpl type;

  /**
   * The constructor.
   */
  public AbstractVirtualBean() {

    this(null);
  }

  /**
   * The constructor.
   *
   * @param type the {@link #getType() type}.
   */
  public AbstractVirtualBean(BeanClass type) {

    super();
    if (type == null) {
      Class<? extends AbstractVirtualBean> javaClass = getClass();
      this.type = BeanClassImpl.asClass(javaClass);
    } else {
      this.type = (BeanClassImpl) type;
    }
    if (this.type.getPrototype() == null) {
      this.type.setPrototype(this);
    }
  }

  @Override
  public boolean isDynamic() {

    return true;
  }

  @Override
  protected boolean isThreadSafe() {

    return isDynamic();
  }

  @Override
  public final boolean isPrototype() {

    return (this.type.getPrototype() == this);
  }

  @Override
  public BeanClass getType() {

    return this.type;
  }

  @Override
  protected AbstractBean create() {

    AbstractVirtualBean bean = (AbstractVirtualBean) super.create();
    bean.type = this.type;
    return bean;
  }

  @Override
  public WritableProperty<?> getProperty(String name) {

    updateProperties();
    return super.getProperty(name);
  }

  @Override
  public int getPropertyCount() {

    updateProperties();
    return super.getPropertyCount();
  }

  @Override
  public Collection<? extends WritableProperty<?>> getProperties() {

    updateProperties();
    return super.getProperties();
  }

  @Override
  protected void onPropertyAdded(WritableProperty<?> property) {

    super.onPropertyAdded(property);
    if (isPrototype()) {
      this.modificationCounter = MODIFICATION_SEQUNCE.incrementAndGet();
    }
  }

  /**
   * Called before properties are accessed if {@link #isDynamic() dynamic} and not {@link #isReadOnly() read-only} to
   * allow implementations to update properties internally before.
   */
  protected void updateProperties() {

    if (isPrototype()) {
      long maxCounter = this.updateCounter;
      for (BeanClass superClass : this.type.getSuperClasses()) {
        AbstractVirtualBean prototype = (AbstractVirtualBean) superClass.getPrototype();
        if ((prototype != null) && prototype.isDynamic()) {
          Iterable<? extends WritableProperty<?>> properties = prototype.getProperties();
          if (prototype.modificationCounter > this.updateCounter) {
            for (WritableProperty<?> property : properties) {
              add(property, AddMode.COPY_WITH_VALUE);
            }
            if (prototype.modificationCounter > maxCounter) {
              maxCounter = prototype.modificationCounter;
            }
          }
        }
      }
      this.updateCounter = maxCounter;
    } else {
      AbstractVirtualBean prototype = VirtualBean.getPrototype(this);
      if ((prototype != null) && prototype.isDynamic()) {
        Iterable<? extends WritableProperty<?>> properties = prototype.getProperties();
        if (this.updateCounter < prototype.modificationCounter) {
          for (WritableProperty<?> property : properties) {
            add(property, AddMode.COPY_WITH_VALUE);
          }
          this.updateCounter = prototype.modificationCounter;
        }
      }
    }
  }

}
