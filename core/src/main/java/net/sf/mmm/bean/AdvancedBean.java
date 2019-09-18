/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import net.sf.mmm.bean.impl.BeanClassImpl;
import net.sf.mmm.property.WritableProperty;

/**
 * Implementation of {@link VirtualBean} as regular java class. Extend your beans from this class if you need virtual
 * and dynamic typing.
 */
public class AdvancedBean extends AbstractBean implements VirtualBean {

  private static final AtomicLong MODIFICATION_SEQUNCE = new AtomicLong(1);

  private static final Map<String, BeanClassImpl> CLASS_MAP = new ConcurrentHashMap<>();

  /** @see BeanClass#getPrototype() */
  public static final AdvancedBean PROTOTYPE = new AdvancedBean();

  private final BeanClassImpl type;

  private long modificationCounter;

  private long updateCounter;

  /**
   * The constructor.
   */
  public AdvancedBean() {

    this(null, false);
  }

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
    Class<? extends AdvancedBean> javaClass = getClass();
    if (type == null) {
      if (writable != null) {
        this.type = ((AdvancedBean) writable).type.getReadOnly();
      } else {
        this.type = CLASS_MAP.computeIfAbsent(javaClass.getName(), (x) -> createBeanClass(javaClass));
        // this.type = BeanClassImpl.of(javaClass);
      }
    } else {
      assert (type.getJavaClass() == javaClass);
      this.type = (BeanClassImpl) type;
      if (this.type.getPrototype() == null) {
        this.type.setPrototype(this);
      }
    }
  }

  /**
   * @param javaClass the {@link Class} reflecting the {@link AdvancedBean}.
   * @return the corresponding {@link BeanClass}.
   */
  private BeanClassImpl getBeanClass(Class<?> javaClass) {

    BeanClassImpl beanClass = CLASS_MAP.get(javaClass.getName());
    if (beanClass == null) {
      throw new IllegalStateException(
          "Super-class " + javaClass + " not registered! It seems you forgot to declare a static prototype instance.");
    }
    return beanClass;
  }

  /**
   * @param javaClass the {@link Class} reflecting the {@link AdvancedBean}.
   * @return the {@link BeanClass} for this instance.
   */
  private BeanClassImpl createBeanClass(Class<? extends VirtualBean> javaClass) {

    List<BeanClassImpl> superClassList = Collections.emptyList();
    if (javaClass.isInterface()) {
      if (javaClass != VirtualBean.class) {
        Class<?>[] interfaces = javaClass.getInterfaces();
        superClassList = new ArrayList<>(interfaces.length);
        for (Class<?> superclass : interfaces) {
          if (VirtualBean.class.isAssignableFrom(superclass)) {
            superClassList.add(getBeanClass(superclass));
          }
        }
      }
    } else {
      if (javaClass != AdvancedBean.class) {
        Class<?> superclass = javaClass.getSuperclass();
        if (AdvancedBean.class.isAssignableFrom(superclass)) {
          superClassList = Collections.singletonList(getBeanClass(superclass));
        }
      }
    }
    BeanClassImpl beanClass = new BeanClassImpl(javaClass, superClassList);
    beanClass.setPrototype(this);
    return beanClass;
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
    this.modificationCounter = MODIFICATION_SEQUNCE.incrementAndGet();
  }

  @Override
  protected void updateProperties() {

    super.updateProperties();
    if (isPrototype()) {
      long maxCounter = this.updateCounter;
      for (BeanClass superClass : this.type.getSuperClasses()) {
        AdvancedBean prototype = (AdvancedBean) superClass.getPrototype();
        if (prototype.isDynamic()) {
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
      AdvancedBean prototype = VirtualBean.getPrototype(this);
      if (prototype.isDynamic()) {
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
