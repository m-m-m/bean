/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl.operation;

import java.lang.reflect.Method;
import java.util.Objects;

import io.github.mmm.base.metainfo.MetaInfo;
import io.github.mmm.bean.Bean;
import io.github.mmm.bean.Mandatory;
import io.github.mmm.bean.PropertyAlias;
import io.github.mmm.bean.factory.impl.GenericTypeInfo;
import io.github.mmm.bean.factory.impl.bean.SimpleBeanAliasAccess;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.bean.factory.impl.typeinfo.PropertyTypeInfoByProperty;
import io.github.mmm.bean.factory.impl.typeinfo.PropertyTypeInfoByValue;
import io.github.mmm.property.PropertyMetadata;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.factory.AbstractSimplePropertyFactory;
import io.github.mmm.property.factory.PropertyFactory;
import io.github.mmm.property.factory.PropertyFactoryManager;
import io.github.mmm.property.factory.PropertyTypeInfo;
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

  private MetaInfo metaInfo;

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
    PropertyFactory factory = PropertyFactoryManager.get().getRequiredFactory(propertyClass, null);
    PropertyTypeInfo typeInfo = null;
    if (factory instanceof AbstractSimplePropertyFactory) {
      return factory.create(this.propertyName, typeInfo, metadata);
    }
    typeInfo = new PropertyTypeInfoByProperty(propertyType, this, proxy);

    WritableProperty<?> property = (WritableProperty<?>) PropertyFactoryManager.get().create(propertyClass, typeInfo,
        this.propertyName, metadata);
    return property;
  }

  /**
   * @param proxy the {@link BeanProxy}.
   * @param valueType the {@link GenericTypeInfo} of the {@link WritableProperty#getValueClass() value class} or
   *        {@code null} if {@code propertyType} is given.
   * @return the new {@link WritableProperty}.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  public WritableProperty<?> createPropertyByValueType(BeanProxy proxy, GenericTypeInfo valueType) {

    PropertyMetadata metadata = createMetadata(proxy, null);
    Class<?> valueClass = valueType.getRawClass();
    PropertyFactory factory = PropertyFactoryManager.get().getRequiredFactory(null, valueClass);
    PropertyTypeInfo typeInfo = null;
    if (factory instanceof AbstractSimplePropertyFactory) {
      return factory.create(this.propertyName, typeInfo, metadata);
    }
    typeInfo = new PropertyTypeInfoByValue(valueType, this, proxy);

    WritableProperty<?> property = (WritableProperty<?>) PropertyFactoryManager.get().create(null, typeInfo,
        this.propertyName, metadata);
    return property;
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
    MetaInfo metaInformation = getMetaInfo();
    if (!metaInformation.isEmpty()) {
      assert (result.getMetaInfo().isEmpty());
      result = result.withMetaInfo(metaInformation);
    }
    return result;
  }

  private MetaInfo getMetaInfo() {

    if (this.metaInfo == null) {
      return getOrCreateMetaInfo();
    }
    return this.metaInfo;
  }

  private synchronized MetaInfo getOrCreateMetaInfo() {

    if (this.metaInfo == null) {
      this.metaInfo = MetaInfo.empty().with(this.method);
    }
    return this.metaInfo;

  }

  @Override
  public void registerAliases(SimpleBeanAliasAccess bean) {

    PropertyAlias aliasAnnotation = this.method.getAnnotation(PropertyAlias.class);
    if (aliasAnnotation != null) {
      bean.registerAliases(getPropertyName(), aliasAnnotation.value());
    }
  }

}
