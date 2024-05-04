package io.github.mmm.bean.impl.properties;

import java.util.Collection;
import java.util.Locale;
import java.util.function.Function;

import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;

/**
 * Interface for the container of {@link WritableProperty properties} for a {@link io.github.mmm.bean.AbstractBean
 * bean}.
 */
public abstract class BeanProperties {

  /**
   * @param name the {@link WritableProperty#getName() name} of the requested {@link WritableProperty property}.
   * @return the requested {@link WritableProperty property} or {@code null} if no such property exists.
   * @see io.github.mmm.bean.ReadableBean#getProperty(String)
   */
  public abstract WritableProperty<?> get(String name);

  /**
   * @return the {@link Collection} with all contained {@link ReadableProperty properties}.
   * @see io.github.mmm.bean.ReadableBean#getProperties()
   */
  public abstract Collection<? extends WritableProperty<?>> get();

  /**
   * Internal method to add a property.
   *
   * @param property the {@link WritableProperty} to add.
   * @see io.github.mmm.bean.WritableBean#addProperty(WritableProperty)
   */
  public abstract void add(WritableProperty<?> property);

  /**
   * Internal method to add a property if absent.
   *
   * @param name the {@link WritableProperty#getName() name} of the property to add.
   * @param factory the {@link Function} used to create the {@link WritableProperty} to add if absent.
   * @return the existing property or the property created by the given {@code factory}.
   * @see io.github.mmm.bean.WritableBean#addProperty(WritableProperty)
   * @see java.util.Map#computeIfAbsent(Object, Function)
   */
  public abstract WritableProperty<?> addIfAbsent(String name, Function<String, WritableProperty<?>> factory);

  /**
   * @param name the potential {@link io.github.mmm.property.WritableProperty#getName() property name}.
   * @return the normalized form for case-insensitive mapping.
   */
  protected static String normalize(String name) {

    return name.toLowerCase(Locale.ROOT);
  }

  /**
   * @return a new {@link BeanPropertyNames} instance with the names of the current properties.
   */
  public BeanPropertyNames createNames() {

    Collection<? extends WritableProperty<?>> collection = get();
    String[] names = new String[collection.size()];
    int i = 0;
    for (WritableProperty<?> property : collection) {
      names[i++] = normalize(property.getName());
    }
    return new BeanPropertyNamesArray(names);
  }
  //
  // /**
  // * @param dynamic the {@link Bean#isDynamic() dynamic flag}.
  // * @param threadSafe
  // * @return the {@link BeanProperties} instance.
  // */
  // public static BeanProperties of(boolean dynamic, boolean threadSafe) {
  //
  // return new BeanPropertiesMap(threadSafe);
  // }

}
