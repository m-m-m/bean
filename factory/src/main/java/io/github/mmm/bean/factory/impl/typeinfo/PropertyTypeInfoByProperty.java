package io.github.mmm.bean.factory.impl.typeinfo;

import io.github.mmm.bean.factory.impl.GenericTypeInfo;
import io.github.mmm.bean.factory.impl.operation.BeanOperationOnProperty;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.container.collection.ReadableCollectionProperty;
import io.github.mmm.property.container.map.ReadableMapProperty;
import io.github.mmm.property.enumeration.ReadableEnumProperty;
import io.github.mmm.property.object.WritableSimpleProperty;
import io.github.mmm.property.range.ReadableRangeProperty;

/**
 * Implementation of {@link AbstractPropertyTypeInfo} by {@link WritableProperty property} {@link GenericTypeInfo type}.
 *
 * @param <V> type of {@link #getValueClass()}.
 */
public class PropertyTypeInfoByProperty<V> extends AbstractPropertyTypeInfo<V> {

  /**
   * The constructor.
   *
   * @param genericType the {@link GenericTypeInfo}.
   * @param operation the {@link BeanOperationOnProperty}.
   * @param proxy the {@link BeanProxy} instance.
   */
  @SuppressWarnings("unchecked")
  public PropertyTypeInfoByProperty(GenericTypeInfo genericType, BeanOperationOnProperty operation, BeanProxy proxy) {

    super(genericType, operation, proxy);
    Class<?> propertyClass = this.owner.getRawClass();
    if (ReadableEnumProperty.class.isAssignableFrom(propertyClass)) {
      if (this.typeArguments.length == 1) {
        this.valueClass = (Class<V>) GenericTypeInfo.getRawClass(this.typeArguments[0], propertyClass);
      }
    } else if (ReadableCollectionProperty.class.isAssignableFrom(propertyClass)) {
      if (this.typeArguments.length == 1) {
        // ListProperty<MyType> --> MyType
        this.valueProperty = getChildProperty(this.typeArguments[0]);
      }
    } else if (ReadableMapProperty.class.isAssignableFrom(propertyClass)) {
      // MapProperty<String, MyType> --> key: String, value: MyType
      if (this.typeArguments.length == 2) {
        this.keyProperty = (WritableSimpleProperty<?>) getChildProperty(this.typeArguments[0]);
        this.valueProperty = getChildProperty(this.typeArguments[1]);
      }
    } else if (ReadableRangeProperty.class.isAssignableFrom(propertyClass)) {
      if (this.typeArguments.length == 1) {
        // RangeProperty<Long> --> Long
        this.valueProperty = getChildProperty(this.typeArguments[0]);
      }
    }
  }

}
