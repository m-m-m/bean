/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

import net.sf.mmm.bean.impl.BeanClassImpl;
import net.sf.mmm.property.WritableProperty;

/**
 * Implementation of {@link VirtualBean} as regular java class. Extend your beans from this class if you need virtual
 * and dynamic typing.
 */
public class AdvancedBean extends AbstractBean implements VirtualBean {

  private final BeanClassImpl type;

  private long updateCounter;

  /**
   * The constructor.
   *
   * @param writable the writable {@link Bean} to create a {@link #isReadOnly() read-only} view on or {@code null} to
   *        create a regular mutable {@link Bean}.
   * @param dynamic the {@link #isDynamic() dynamic flag}.
   */
  public AdvancedBean(AbstractBean writable, boolean dynamic) {

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
  public AdvancedBean(AbstractBean writable, boolean dynamic, BeanClass type) {

    super(writable, dynamic);
    if (type == null) {
      if (writable != null) {
        BeanClassImpl beanClass = (BeanClassImpl) writable.getType();
        this.type = WritableBean.getReadOnly(beanClass);
      } else {
        this.type = BeanClassImpl.of(getClass());
      }
    } else {
      assert (type.getJavaClass() == getClass());
      this.type = (BeanClassImpl) type;
    }
  }

  @Override
  public final boolean isClass() {

    return false;
  }

  @Override
  public BeanClass getType() {

    this.type.setInitialized();
    return this.type;
  }

  @Override
  protected <P extends WritableProperty<?>> P add(P property, AddMode mode) {

    P result = super.add(property, mode);
    if ((mode == AddMode.INTERNAL) && !this.type.isInitialized() && !isReadOnly()) {
      this.type.add(property, AddMode.COPY);
    }
    return result;
  }

  @Override
  protected void updateProperties() {

    super.updateProperties();
    if (this.type.isDynamic()) {
      Iterable<? extends WritableProperty<?>> properties = this.type.getProperties();
      long modificationCounter = this.type.getModificationCounter();
      if (this.updateCounter < modificationCounter) {
        for (WritableProperty<?> property : properties) {
          add(property, AddMode.COPY_WITH_VALUE);
        }
        this.updateCounter = modificationCounter;
      }
    }
  }

}
