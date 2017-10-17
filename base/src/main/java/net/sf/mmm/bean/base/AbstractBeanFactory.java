/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.base;

import net.sf.mmm.bean.api.BeanFactory;
import net.sf.mmm.util.component.api.AlreadyInitializedException;
import net.sf.mmm.util.component.api.NotInitializedException;
import net.sf.mmm.util.component.base.AbstractComponent;

/**
 * Abstract base implementation of {@link BeanFactory}.
 *
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 * @since 1.0.0
 */
public abstract class AbstractBeanFactory extends AbstractComponent implements BeanFactory {

  private static BeanFactory instance;

  /**
   * The constructor.
   */
  public AbstractBeanFactory() {

    super();
  }

  /**
   * This method gets the singleton instance of this {@link BeanFactory}. <br>
   * <b>ATTENTION:</b><br>
   * Please read {@link net.sf.mmm.util.component.api.Cdi#GET_INSTANCE} before using.
   *
   * @return the singleton instance.
   */
  public static BeanFactory getInstance() {

    if (instance == null) {
      throw new NotInitializedException();
    }
    return instance;
  }

  /**
   * @return {@code true} if {@link #getInstance() instance} is defined, {@code false} otherwise (if
   *         {@code null}).
   */
  protected static boolean hasInstance() {

    return (instance != null);
  }

  /**
   * @param factory the {@link BeanFactory} to set.
   */
  public static void setInstance(BeanFactory factory) {

    if (instance != null) {
      throw new AlreadyInitializedException();
    }
    instance = factory;
  }

}
