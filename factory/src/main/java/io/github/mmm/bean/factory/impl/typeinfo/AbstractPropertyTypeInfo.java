package io.github.mmm.bean.factory.impl.typeinfo;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.github.mmm.bean.factory.impl.GenericTypeInfo;
import io.github.mmm.bean.factory.impl.operation.BeanOperationOnProperty;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.factory.PropertyTypeInfo;
import io.github.mmm.property.object.WritableSimpleProperty;

/**
 * Abstract base class for real implementations of {@link PropertyTypeInfo}.
 *
 * @param <V> type of {@link #getValueClass()}.
 */
public abstract class AbstractPropertyTypeInfo<V> implements PropertyTypeInfo<V> {

  private static final Type[] NO_TYPES = new Type[0];

  /** The {@link GenericTypeInfo} of the property or value. */
  protected final GenericTypeInfo owner;

  private final BeanOperationOnProperty operation;

  private final BeanProxy proxy;

  /** @see #getTypeArgument(int) */
  protected final Type[] typeArguments;

  /** @see #getValueClass() */
  protected Class<V> valueClass;

  /** @see #getValueProperty() */
  protected WritableProperty<?> valueProperty;

  /** @see #getKeyProperty() */
  protected WritableSimpleProperty<?> keyProperty;

  /**
   * The constructor.
   *
   * @param genericType the {@link GenericTypeInfo}.
   * @param operation the {@link BeanOperationOnProperty}.
   * @param proxy the {@link BeanProxy} instance.
   */
  public AbstractPropertyTypeInfo(GenericTypeInfo genericType, BeanOperationOnProperty operation, BeanProxy proxy) {

    super();
    this.owner = genericType;
    this.operation = operation;
    this.proxy = proxy;
    Type type = this.owner.getGenericType();
    if (type instanceof ParameterizedType pType) {
      this.typeArguments = pType.getActualTypeArguments();
    } else {
      this.typeArguments = NO_TYPES;
    }
  }

  @Override
  public Class<?> getOwnerClass() {

    return this.owner.getRawClass();
  }

  @Override
  public Type getOwnerType() {

    return this.owner.getGenericType();
  }

  @Override
  public Type getTypeArgument(int i) {

    if ((i >= 0) && (i < this.typeArguments.length)) {
      return this.typeArguments[i];
    }
    return null;
  }

  @Override
  public Class<?> getTypeArgumentClass(int i) {

    if ((i >= 0) && (i < this.typeArguments.length)) {
      return GenericTypeInfo.getRawClass(this.typeArguments[i], this.owner.getRawClass());
    }
    return null;
  }

  WritableProperty<?> getChildProperty(Type childType) {

    GenericTypeInfo childGenericType = GenericTypeInfo.ofGeneric(childType, this.owner.getGenericType());
    if (childGenericType != null) {
      return this.operation.createPropertyByValueType(this.proxy, childGenericType);
    }
    return null;
  }

  @Override
  public Class<V> getValueClass() {

    return this.valueClass;
  }

  @Override
  public WritableProperty<?> getValueProperty() {

    return this.valueProperty;
  }

  @Override
  public WritableSimpleProperty<?> getKeyProperty() {

    return this.keyProperty;
  }

}
