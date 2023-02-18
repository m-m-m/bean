/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.operation;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Collection;
import java.util.Objects;

import io.github.mmm.bean.Bean;
import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.bean.factory.impl.GenericTypeInfo;
import io.github.mmm.bean.factory.impl.bean.SimpleBeanAliasAccess;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.property.PropertyMetadata;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.container.collection.ReadableCollectionProperty;
import io.github.mmm.property.factory.PropertyFactoryManager;
import io.github.mmm.validation.Validator;
import io.github.mmm.validation.main.ValidatorMandatory;

/**
 * Operation on a {@link Bean} {@link WritableProperty property}.
 *
 * @since 1.0.0
 */
public abstract class BeanOperationOnProperty extends BeanOperation {

  /** @see #getPropertyName() */
  protected final String propertyName;

  /** @see #getMethod() */
  protected final Method method;

  /**
   * The constructor.
   *
   * @param propertyName the {@link #getPropertyName() propertyName}.
   * @param method the {@link #getMethod() method}.
   */
  public BeanOperationOnProperty(String propertyName, Method method) {

    super();
    Objects.requireNonNull(propertyName, "propertyName");
    this.propertyName = propertyName;
    this.method = method;
  }

  @Override
  public String getPropertyName() {

    return this.propertyName;
  }

  /**
   * @return the {@link Method} of this operation.
   */
  public Method getMethod() {

    return this.method;
  }

  /**
   * @param proxy the {@link BeanProxy}.
   * @param propertyType the {@link GenericTypeInfo} of the the {@link WritableProperty} or {@code null} if
   *        {@code valueType} is given.
   * @return the new {@link WritableProperty}.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected WritableProperty<?> createPropertyByPropertyType(BeanProxy proxy, GenericTypeInfo propertyType) {

    PropertyMetadata metadata = createMetadata(proxy, null);
    Class propertyClass = propertyType.getRawClass();
    WritableProperty<?> valueProperty = null;
    if (ReadableCollectionProperty.class.isAssignableFrom(propertyClass)) {
      Type type = propertyType.getGenericType();
      if (type instanceof ParameterizedType) {
        Type[] generics = ((ParameterizedType) type).getActualTypeArguments();
        if (generics.length == 1) {
          Type childType = generics[0];
          GenericTypeInfo childGenericType = asRawClass(childType, childType);
          if (childGenericType != null) {
            valueProperty = createPropertyByValueType(proxy, childGenericType);
          }
        }
      }
    }
    WritableProperty<?> property = (WritableProperty<?>) PropertyFactoryManager.get().create(propertyClass, null,
        this.propertyName, metadata, valueProperty);
    return property;
  }

  /**
   * @param proxy the {@link BeanProxy}.
   * @param valueType the {@link GenericTypeInfo} of the {@link WritableProperty#getValueClass() value class} or
   *        {@code null} if {@code propertyType} is given.
   * @return the new {@link WritableProperty}.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  protected WritableProperty<?> createPropertyByValueType(BeanProxy proxy, GenericTypeInfo valueType) {

    PropertyMetadata metadata = createMetadata(proxy, null);
    WritableProperty valueProperty = null;
    Class<?> valueClass = valueType.getRawClass();
    if (Collection.class.isAssignableFrom(valueClass)) {
      Type type = valueType.getGenericType();
      if (type instanceof ParameterizedType) {
        Type[] generics = ((ParameterizedType) type).getActualTypeArguments();
        if (generics.length == 1) {
          Type childType = generics[0];
          GenericTypeInfo childGenericType = asRawClass(childType, childType);
          if (childGenericType != null) {
            valueProperty = createPropertyByValueType(proxy, childGenericType);
          }
        }
      }
    }

    WritableProperty<?> property = (WritableProperty<?>) PropertyFactoryManager.get().create(null, valueClass,
        this.propertyName, metadata, valueProperty);
    return property;
  }

  // We are fully aware that this in not a correct solution for the problem.
  // However, the JDK does not offer an API to solve this problem and we already solved it earlier but with a very
  // high complexity. As we actually want to go away from deep reflection, we avoid the complexity here.
  // If you have a bean using a generic returning ListProperty<T> this will simply not be able to resolve the real class
  // for T
  private GenericTypeInfo asRawClass(Type type, Type root) {

    if (type instanceof Class) {
      return GenericTypeInfo.of((Class<?>) type, root);
    } else if (type instanceof ParameterizedType) {
      return asRawClass(((ParameterizedType) type).getRawType(), root);
    } else if (type instanceof WildcardType) {
      Type[] bounds = ((WildcardType) type).getUpperBounds();
      if (bounds.length > 0) {
        return asRawClass(bounds[0], root);
      }
    }
    return null;
  }

  /**
   * @param proxy the {@link BeanProxy}.
   * @param metadata the optional existing {@link PropertyMetadata} to use as template. May be {@code null}.
   * @return the new {@link PropertyMetadata}.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected PropertyMetadata createMetadata(BeanProxy proxy, PropertyMetadata metadata) {

    PropertyMetadata result;
    if (metadata == null) {
      Validator validator = Validator.none();
      if (this.method.isAnnotationPresent(Mandatory.class)) {
        validator = ValidatorMandatory.get();
      }
      result = PropertyMetadata.of(proxy.getProxy(), validator);
    } else {
      result = metadata.withLock(proxy.getProxy());
      Validator validator = metadata.getValidator();
      if (!validator.isMandatory()) {
        if (this.method.isAnnotationPresent(Mandatory.class)) {
          validator = validator.append(ValidatorMandatory.get());
          result = result.withValidator(validator);
        }
      }
    }
    return result;
  }

  @Override
  public void registerAliases(SimpleBeanAliasAccess bean) {

    PropertyAlias aliasAnnotation = this.method.getAnnotation(PropertyAlias.class);
    if (aliasAnnotation != null) {
      bean.registerAliases(getPropertyName(), aliasAnnotation.value());
    }
  }

}
