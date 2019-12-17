/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import java.util.concurrent.atomic.AtomicLong;

import io.github.mmm.bean.impl.BeanClassImpl;
import io.github.mmm.property.WritableProperty;

/**
 * Implementation of {@link VirtualBean} as regular java class. Extend your beans from this class if you need virtual
 * and dynamic typing.
 */
public abstract class AbstractVirtualBean extends AbstractBean implements VirtualBean {

  private static final AtomicLong MODIFICATION_SEQUNCE = new AtomicLong(1);

  private final BeanClassImpl type;

  private long modificationCounter;

  private long updateCounter;

  /**
   * The constructor.
   */
  public AbstractVirtualBean() {

    this(null, false);
  }

  /**
   * The constructor.
   *
   * @param writable the writable {@link Bean} to create a {@link #isReadOnly() read-only} view on or {@code null} to
   *        create a regular mutable {@link Bean}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   */
  public AbstractVirtualBean(AbstractBean writable, boolean dynamic) {

    this(writable, dynamic, null);
  }

  /**
   * The constructor.
   *
   * @param writable the writable {@link Bean} to create a {@link #isReadOnly() read-only} view on or {@code null} to
   *        create a regular mutable {@link Bean}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   * @param type the {@link #getType() type}.
   */
  public AbstractVirtualBean(AbstractBean writable, boolean dynamic, BeanClass type) {

    super(writable, dynamic);
    Class<? extends AbstractVirtualBean> javaClass = getClass();
    if (type == null) {
      if (writable != null) {
        this.type = ((AbstractVirtualBean) writable).type.getReadOnly();
      } else {
        this.type = BeanClassImpl.asClass(javaClass);
        if (this.type.getPrototype() == null) {
          this.type.setPrototype(this);
        }
      }
    } else {
      this.type = (BeanClassImpl) type;
      if (this.type.getPrototype() == null) {
        this.type.setPrototype(this);
      }
    }
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
  protected void onPropertyAdded(WritableProperty<?> property) {

    super.onPropertyAdded(property);
    if (isPrototype()) {
      this.modificationCounter = MODIFICATION_SEQUNCE.incrementAndGet();
    }
  }

  @Override
  protected void updateProperties() {

    super.updateProperties();
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
