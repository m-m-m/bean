/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.property;

import java.util.Objects;

import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.Property;
import io.github.mmm.property.PropertyMetadata;
import io.github.mmm.value.converter.TypeMapper;

/**
 * Implementation of {@link WritableBeanProperty}.
 *
 * @param <V> type of the {@link WritableBean bean} {@link #getValue() value}.
 * @since 1.0.0
 */
public class BeanProperty<V extends WritableBean> extends Property<V> implements WritableBeanProperty<V> {

  private final Class<V> valueClass;

  private V value;

  private TypeMapper<V, ?> typeMapper;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param valueClass the {@link #getValueClass() value class}.
   */
  public BeanProperty(String name, Class<V> valueClass) {

    this(name, valueClass, null);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param valueClass the {@link #getValueClass() value class}.
   * @param metadata the {@link #getMetadata() metadata}.
   */
  public BeanProperty(String name, Class<V> valueClass, PropertyMetadata<V> metadata) {

    super(name, metadata);
    this.valueClass = valueClass;
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param value the (initial) {@link #get() value}.
   */
  public BeanProperty(String name, V value) {

    this(name, value, null);
  }

  /**
   * The constructor.
   *
   * @param name the {@link #getName() name}.
   * @param value the (initial) {@link #get() value}.
   * @param metadata the {@link #getMetadata() metadata}.
   */
  @SuppressWarnings("unchecked")
  public BeanProperty(String name, V value, PropertyMetadata<V> metadata) {

    super(name, metadata);
    Objects.requireNonNull(value);
    this.value = value;
    this.valueClass = (Class<V>) this.value.getClass();
  }

  @Override
  public boolean isValueMutable() {

    if (this.value != null) {
      return !this.value.isReadOnly();
    }
    return false;
  }

  @Override
  protected V doGet() {

    return this.value;
  }

  @Override
  protected void doSet(V newValue) {

    this.value = newValue;
    if (newValue != null) {
      newValue.parentPath(this);
      this.typeMapper = null;
    }
  }

  @Override
  public Class<V> getValueClass() {

    return this.valueClass;
  }

  @Override
  public TypeMapper<V, ?> getTypeMapper() {

    if (this.typeMapper == null) {
      V bean = get();
      if (bean != null) {
        this.typeMapper = BeanTypeMapper.of(bean);
      }
    }
    return this.typeMapper;
  }

}
