/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

/**
 * Implementation of {@link VirtualBean} as regular java class. Extend your beans from this class if you need virtual
 * and dynamic typing.
 */
public class AdvancedBean extends AbstractVirtualBean {

  /** @see BeanClass#getPrototype() */
  public static final AdvancedBean PROTOTYPE = new AdvancedBean();

  /**
   * The constructor.
   */
  public AdvancedBean() {

    super();
  }

  /**
   * The constructor.
   *
   * @param type the {@link #getType() type}.
   */
  public AdvancedBean(BeanClass type) {

    super(type);
    Class<? extends AdvancedBean> javaClass = getClass();
    if ((type != null) && (type.getJavaClass() != javaClass)) {
      throw new IllegalArgumentException(
          "BeanClass " + type.getJavaClass() + " has to match bean implementation class " + javaClass.getName());
    }
  }

}
