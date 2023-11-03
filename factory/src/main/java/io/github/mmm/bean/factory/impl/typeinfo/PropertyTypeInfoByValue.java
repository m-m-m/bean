package io.github.mmm.bean.factory.impl.typeinfo;

import java.util.Collection;
import java.util.Map;

import io.github.mmm.base.range.Range;
import io.github.mmm.bean.factory.impl.GenericTypeInfo;
import io.github.mmm.bean.factory.impl.operation.BeanOperationOnProperty;
import io.github.mmm.bean.factory.impl.proxy.BeanProxy;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.object.WritableSimpleProperty;

/**
 * Implementation of {@link AbstractPropertyTypeInfo} by {@link WritableProperty property} {@link GenericTypeInfo type}.
 *
 * @param <V> type of {@link #getValueClass()}.
 */
public class PropertyTypeInfoByValue<V> extends AbstractPropertyTypeInfo<V> {

  /**
   * The constructor.
   *
   * @param genericType the {@link GenericTypeInfo}.
   * @param operation the {@link BeanOperationOnProperty}.
   * @param proxy the {@link BeanProxy} instance.
   */
  @SuppressWarnings("unchecked")
  public PropertyTypeInfoByValue(GenericTypeInfo genericType, BeanOperationOnProperty operation, BeanProxy proxy) {

    super(genericType, operation, proxy);
    this.valueClass = (Class<V>) this.owner.getRawClass();
    if (Collection.class.isAssignableFrom(this.valueClass)) {
      if (this.typeArguments.length == 1) {
        // List<MyType> --> MyType
        this.valueProperty = getChildProperty(this.typeArguments[0]);
      }
    } else if (Map.class.isAssignableFrom(this.valueClass)) {
      if (this.typeArguments.length == 2) {
        // Map<String, MyType> --> key:String, value:MyType
        this.keyProperty = (WritableSimpleProperty<?>) getChildProperty(this.typeArguments[0]);
        this.valueProperty = getChildProperty(this.typeArguments[1]);
      }
    } else if (Range.class.isAssignableFrom(this.valueClass)) {
      if (this.typeArguments.length == 1) {
        // Range<Long> --> Long
        this.valueProperty = getChildProperty(this.typeArguments[0]);
      }
    }
  }

}
