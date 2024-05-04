package io.github.mmm.bean.impl.properties;

import java.util.AbstractCollection;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Function;

import io.github.mmm.property.WritableProperty;

/**
 * Implementation of {@link BeanProperties} using {@link List}.
 */
public class BeanPropertiesDynamicArray extends BeanProperties {

  private int size;

  @SuppressWarnings("rawtypes")
  private WritableProperty[] propertiesArray;

  private final PropertiesCollection properties;

  /**
   * The constructor.
   *
   * @param capacity the initial capacity.
   */
  public BeanPropertiesDynamicArray(int capacity) {

    super();
    if (capacity < 8) {
      capacity = 8;
    }
    this.propertiesArray = new WritableProperty[capacity];
    this.properties = new PropertiesCollection();
  }

  @Override
  public WritableProperty<?> get(String name) {

    int index = indexedBinarySearch(name);
    if (index >= 0) {
      return this.propertiesArray[index];
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
    int high = this.size - 1;

    while (low <= high) {
      int mid = (low + high) >>> 1;
      WritableProperty<?> midVal = this.propertiesArray[mid];
      String currentName = normalize(midVal.getName()); // TODO store in metadata and make accessible for efficiency!
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
      insert(~index, property);
    } else {
      this.propertiesArray[index] = property;
    }
  }

  @SuppressWarnings("rawtypes")
  private void insert(int index, WritableProperty<?> property) {

    assert (index <= this.size);
    WritableProperty[] source = this.propertiesArray;
    WritableProperty[] target = this.propertiesArray;
    int capacity = this.propertiesArray.length;
    if (this.size == capacity) {
      int addCapacity;
      if (capacity <= 32) {
        addCapacity = 4;
      } else {
        // here we use a power of 2 (8=2^3) so compiler will optimize to shift
        addCapacity = capacity / 8;
      }
      target = new WritableProperty[capacity + addCapacity];
    }
    if (source != target) {
      System.arraycopy(source, 0, target, 0, index);
    }
    int len = this.size - index;
    if (len > 0) {
      System.arraycopy(source, index, target, index + 1, len);
    }
    target[index] = property;
    if (this.propertiesArray != target) {
      this.propertiesArray = target;
    }
    this.size++;
    // verification
    for (int i = 0; i < this.size; i++) {
      if (this.propertiesArray[i] == null) {
        throw new IllegalStateException(Arrays.toString(this.propertiesArray));
      }
    }
  }

  @Override
  public WritableProperty<?> addIfAbsent(String name, Function<String, WritableProperty<?>> factory) {

    int index = indexedBinarySearch(name);
    WritableProperty<?> result;
    if (index < 0) {
      result = factory.apply(name);
      insert(~index, result);
    } else {
      result = this.propertiesArray[index];
    }
    return result;
  }

  private class PropertiesCollection extends AbstractCollection<WritableProperty<?>> {

    private PropertiesCollection() {

      super();
    }

    @Override
    public int size() {

      return BeanPropertiesDynamicArray.this.size;
    }

    @Override
    public boolean remove(Object o) {

      throw new UnsupportedOperationException();
    }

    @Override
    public Iterator<WritableProperty<?>> iterator() {

      return new PropertiesIterator();
    }
  }

  private class PropertiesIterator implements Iterator<WritableProperty<?>> {

    private int index;

    @Override
    public boolean hasNext() {

      return this.index < BeanPropertiesDynamicArray.this.size;
    }

    @Override
    public WritableProperty<?> next() {

      if (this.index >= BeanPropertiesDynamicArray.this.size) {
        throw new NoSuchElementException();
      }
      return BeanPropertiesDynamicArray.this.propertiesArray[this.index++];
    }

  }

}
