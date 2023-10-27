package io.github.mmm.bean.impl.properties;

import java.util.Locale;

/**
 * Abstract base implementation of {@link BeanProperties}.
 */
public abstract class AbstractBeanProperties implements BeanProperties {

  /**
   * @param name the potential {@link io.github.mmm.property.WritableProperty#getName() property name}.
   * @return the normalized form for case-insensitive mapping.
   */
  protected static String normalize(String name) {

    return name.toLowerCase(Locale.ROOT);
  }
}
