package io.github.mmm.bean.impl.properties;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.function.Function;

import io.github.mmm.property.WritableProperty;

/**
 * Implementation of {@link BeanProperties} based on a {@link Map}.
 */
public class BeanPropertiesMap extends BeanProperties {

  private final Map<String, WritableProperty<?>> propertiesMap;

  private final Collection<WritableProperty<?>> properties;

  /**
   * The constructor.
   *
   * @param threadSafe - {@code true} if thread safe behavior is required for concurrent access, {@code false}
   *        otherwise.
   */
  public BeanPropertiesMap(boolean threadSafe) {

    super();
    if (threadSafe) {
      this.propertiesMap = new ConcurrentSkipListMap<>();
    } else {
      this.propertiesMap = new TreeMap<>();
    }
    this.properties = Collections.unmodifiableCollection(this.propertiesMap.values());
  }

  @Override
  public WritableProperty<?> get(String name) {

    String key = normalize(name);
    return this.propertiesMap.get(key);
  }

  @Override
  public Collection<? extends WritableProperty<?>> get() {

    return this.properties;
  }

  @Override
  public void add(WritableProperty<?> property) {

    String key = normalize(property.getName());
    WritableProperty<?> existing = this.propertiesMap.putIfAbsent(key, property);
    if (existing != null) {
      throw new IllegalArgumentException("Duplicate property " + property.getName());
    }
  }

  @Override
  public WritableProperty<?> addIfAbsent(String name, Function<String, WritableProperty<?>> factory) {

    String key = normalize(name);
    return this.propertiesMap.computeIfAbsent(key, factory);
  }

}
