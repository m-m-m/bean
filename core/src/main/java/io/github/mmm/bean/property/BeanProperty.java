/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.property;

import java.util.Objects;
import java.util.function.Supplier;

import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.ReadableBean;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.Property;
import io.github.mmm.property.PropertyMetadata;
import io.github.mmm.property.ReadableProperty;
import io.github.mmm.value.ReadableValue;
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

    if ((this.value == null) && (this.valueClass != null)) {
      doSet(BeanFactory.get().create(this.valueClass));
      fireEvent();
    }
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
  public void copyValue(ReadableValue<V> other) {

    V bean = other.get();
    if (bean != null) {
      bean = ReadableBean.copy(bean);
    }
    set(bean);
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

  @Override
  protected Supplier<? extends V> createReadOnlyExpression() {

    return () -> {
      V bean = get();
      if (bean == null) {
        return null;
      }
      return WritableBean.getReadOnly(bean);
    };
  }

  @Override
  public boolean isEqual(ReadableProperty<?> obj) {

    if (this == obj) {
      return true;
    } else if ((obj == null) || (getClass() != obj.getClass())) {
      return false;
    }
    BeanProperty<?> other = (BeanProperty<?>) obj;
    if (!Objects.equals(getName(), other.getName())) {
      return false;
    } else if ((this.value == null) || (other.value == null)) {
      return this.value == other.value;
    } else if (!this.value.isEqual(other.value)) {
      return false;
    }
    return true;
  }

}
