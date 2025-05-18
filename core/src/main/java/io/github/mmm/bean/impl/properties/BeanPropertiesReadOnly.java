package io.github.mmm.bean.impl.properties;

import java.util.AbstractCollection;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

import io.github.mmm.base.exception.ReadOnlyException;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.WritableProperty;

/**
 * Implementation of {@link BeanProperties} for a read-only bean.
 */
public class BeanPropertiesReadOnly extends BeanProperties {

  private final WritableBean delegate;

  private final Collection<WritableProperty<?>> properties;

  /**
   * The constructor.
   *
   * @param delegate the {@link BeanProperties} to wrap read-only.
   */
  public BeanPropertiesReadOnly(WritableBean delegate) {

    super();
    this.delegate = delegate;
    this.properties = new ReadOnlyPropertiesCollection();
  }

  @Override
  public WritableProperty<?> get(String name) {

    WritableProperty<?> property = this.delegate.getProperty(name);
    if (property == null) {
      return null;
    }
    return property.getReadOnly();
  }

  @Override
  public Collection<? extends WritableProperty<?>> get() {

    return this.properties;
  }

  @Override
  public void add(WritableProperty<?> property) {

    throw new ReadOnlyException(BeanPropertiesReadOnly.class);
  }

  @Override
  public WritableProperty<?> addIfAbsent(String name, Function<String, WritableProperty<?>> factory) {

    throw new ReadOnlyException(BeanPropertiesReadOnly.class);
  }

  private class ReadOnlyPropertiesCollection extends AbstractCollection<WritableProperty<?>> {

    @Override
    public Iterator<WritableProperty<?>> iterator() {

      return new ReadOnlyPropertiesIterator(BeanPropertiesReadOnly.this.delegate.getProperties().iterator());
    }

    @Override
    public int size() {

      return BeanPropertiesReadOnly.this.delegate.getProperties().size();
    }

  }

  private static class ReadOnlyPropertiesIterator implements Iterator<WritableProperty<?>> {

    private final Iterator<? extends WritableProperty<?>> iterator;

    private ReadOnlyPropertiesIterator(Iterator<? extends WritableProperty<?>> iterator) {

      super();
      this.iterator = iterator;
    }

    @Override
    public boolean hasNext() {

      return this.iterator.hasNext();
    }

    @Override
    public WritableProperty<?> next() {

      return this.iterator.next().getReadOnly();
    }

  }

}
