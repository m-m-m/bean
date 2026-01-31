package io.github.mmm.bean.factory.impl.mapper;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.bean.factory.scanner.BeanScanner;

/**
 * Class to determine {@link #getBeanTypes() bean types}.
 *
 * @since 1.0.0
 */
public class BeanTypeSet {

  static final BeanTypeSet INSTANCE = new BeanTypeSet();

  private final Set<Class<? extends WritableBean>> beanTypes;

  /**
   * The constructor.
   */
  public BeanTypeSet() {

    super();
    BeanScanner scanner = new BeanScanner();
    Set<Class<? extends WritableBean>> set = new HashSet<>(scanner.findBeanInterfaces());
    set.addAll(scanner.findBeanClasses());
    this.beanTypes = Collections.unmodifiableSet(set);
  }

  /**
   * @return the {@link Set} with all available {@link WritableBean}s.
   */
  public Set<Class<? extends WritableBean>> getBeanTypes() {

    return this.beanTypes;
  }

}
