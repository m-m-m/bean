package io.github.mmm.bean.impl.properties;

/**
 * Implementation of {@link BeanPropertyNames}.
 */
public class BeanPropertyNamesArray implements BeanPropertyNames {

  private final String[] names;

  /**
   * The constructor.
   *
   * @param names the static propery names in natural order.
   */
  public BeanPropertyNamesArray(String... names) {

    super();
    this.names = names;
  }

  @Override
  public int indexOf(String name) {

    String key = AbstractBeanProperties.normalize(name);
    int low = 0;
    int high = this.names.length - 1;

    while (low <= high) {
      int mid = (low + high) >>> 1;
      String currentName = this.names[mid];
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
  public int size() {

    return this.names.length;
  }

}
