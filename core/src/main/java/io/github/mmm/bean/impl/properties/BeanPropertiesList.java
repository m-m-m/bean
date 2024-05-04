package io.github.mmm.bean.impl.properties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

import io.github.mmm.property.WritableProperty;

/**
 * Implementation of {@link BeanProperties} using {@link List}.
 */
public class BeanPropertiesList extends BeanProperties {

  private final List<WritableProperty<?>> propertiesList;

  private final Collection<WritableProperty<?>> properties;

  /**
   * The constructor.
   *
   * @param threadSafe - {@code true} if thread safe behavior is required for concurrent access, {@code false}
   *        otherwise.
   */
  public BeanPropertiesList(boolean threadSafe) {

    super();
    if (threadSafe) {
      this.propertiesList = new CopyOnWriteArrayList<>();
    } else {
      this.propertiesList = new ArrayList<>();
    }
    this.properties = Collections.unmodifiableCollection(this.propertiesList);
  }

  @Override
  public WritableProperty<?> get(String name) {

    for (WritableProperty<?> property : this.propertiesList) {
      if (property.getName().equalsIgnoreCase(name)) {
        return property;
      }
    }
    return null;
  }

  @Override
  public Collection<? extends WritableProperty<?>> get() {

    return this.properties;
  }

  private int indexedBinarySearch(String name) {

    String key = normalize(name);
    int low = 0;
    int high = this.propertiesList.size() - 1;

    while (low <= high) {
      int mid = (low + high) >>> 1;
      WritableProperty<?> midVal = this.propertiesList.get(mid);
      String currentName = normalize(midVal.getName());
      int cmp = currentName.compareTo(key);
      if (cmp < 0) {
        low = mid + 1;
      } else if (cmp > 0) {
        high = mid - 1;
      } else {
        return mid;
      }

    }
    return -(low + 1);
  }

  @Override
  public void add(WritableProperty<?> property) {

    int index = indexedBinarySearch(property.getName());
    if (index < 0) {
      index = ~index;
    }
    this.propertiesList.add(index, property);
  }

  @Override
  public WritableProperty<?> addIfAbsent(String name, Function<String, WritableProperty<?>> factory) {

    int index = indexedBinarySearch(name);
    if (index >= 0) {
      return this.propertiesList.get(index);
    } else {
      index = ~index;
      WritableProperty<?> property = factory.apply(name);
      this.propertiesList.add(index, property);
      return property;
    }
  }

}
