package io.github.mmm.bean.impl.properties;

import java.util.Collection;
import java.util.Locale;
import java.util.function.Function;

import io.github.mmm.property.ReadableProperty;
import io.github.mmm.property.WritableProperty;

/// Interface for the container of [properties][WritableProperty] for a [bean][io.github.mmm.bean.AbstractBean].
public abstract class BeanProperties {

  /// @param name the [name][WritableProperty#getName()] of the requested [property][WritableProperty].
  /// @return the requested [property][WritableProperty] or `null` if no such property exists.
  /// @see io.github.mmm.bean.ReadableBean#getProperty(String)
  public abstract WritableProperty<?> get(String name);

  /// @return the [Collection] with all contained [properties][ReadableProperty].
  /// @see io.github.mmm.bean.ReadableBean#getProperties()
  public abstract Collection<? extends WritableProperty<?>> get();

  /// Internal method to add a property.
  ///
  /// @param property the [WritableProperty] to add.
  /// @see io.github.mmm.bean.WritableBean#addProperty(WritableProperty)
  public abstract void add(WritableProperty<?> property);

  /// Internal method to add a property if absent.
  ///
  /// @param name the [name][WritableProperty#getName()] of the property to add.
  /// @param factory the [Function] used to create the [WritableProperty] to add if absent.
  /// @return the existing property or the property created by the given `factory`.
  /// @see io.github.mmm.bean.WritableBean#addProperty(WritableProperty)
  /// @see java.util.Map#computeIfAbsent(Object, Function)
  public abstract WritableProperty<?> addIfAbsent(String name, Function<String, WritableProperty<?>> factory);

  /// @param name the potential [property name][io.github.mmm.property.WritableProperty#getName()].
  /// @return the normalized form for case-insensitive mapping.
  protected static String normalize(String name) {

    return name.toLowerCase(Locale.ROOT);
  }

  /// @return a new [BeanPropertyNames] instance with the names of the current properties.
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
