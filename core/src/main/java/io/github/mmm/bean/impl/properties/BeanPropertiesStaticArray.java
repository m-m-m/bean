package io.github.mmm.bean.impl.properties;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.property.WritableProperty;

/**
 * Implementation of {@link BeanProperties} using a static array of {@link WritableProperty}. May only be used for
 * static (non {@link io.github.mmm.bean.Bean#isDynamic() dynamic}) {@link io.github.mmm.bean.Bean}s.
 */
public class BeanPropertiesStaticArray extends BeanProperties {

  private final BeanPropertyNames propertyNames;

  @SuppressWarnings("rawtypes")
  private final WritableProperty[] propertiesArray;

  private final Collection<WritableProperty<?>> properties;

  /**
   * The constructor.
   *
   * @param propertyNames the {@link BeanPropertyNames}.
   */
  public BeanPropertiesStaticArray(BeanPropertyNames propertyNames) {

    super();
    Objects.requireNonNull(propertyNames);
    this.propertyNames = propertyNames;
    this.propertiesArray = new WritableProperty[propertyNames.size()];
    List<WritableProperty<?>> propertiesList = Arrays.asList(this.propertiesArray);
    this.properties = Collections.unmodifiableCollection(propertiesList);
  }

  @Override
  public WritableProperty<?> get(String name) {

    int index = this.propertyNames.indexOf(name);
    if (index < 0) {
      return null;
    }
    return this.propertiesArray[index];
  }

  @Override
  public Collection<? extends WritableProperty<?>> get() {

    return this.properties;
  }

  @Override
  public void add(WritableProperty<?> property) {

    String name = property.getName();
    int index = this.propertyNames.indexOf(name);
    if (index < 0) {
      throw new IllegalArgumentException("Undefined property " + name);
    }
    if (this.propertiesArray[index] != null) {
      throw new DuplicateObjectException(property);
    }
    this.propertiesArray[index] = property;
  }

  @Override
  public WritableProperty<?> addIfAbsent(String name, Function<String, WritableProperty<?>> factory) {

    int index = this.propertyNames.indexOf(name);
    if (index < 0) {
      throw new IllegalArgumentException("Undefined property " + name);
    }
    if (this.propertiesArray[index] == null) {
      this.propertiesArray[index] = factory.apply(name);
    }
    return this.propertiesArray[index];
  }

}
