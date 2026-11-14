package io.github.mmm.bean.property;

import java.util.Iterator;

import io.github.mmm.base.lang.Builder;
import io.github.mmm.bean.BeanBuilder;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.value.converter.CompositeTypeMapper;

/// [CompositeTypeMapper] for a [WritableBean].
///
/// @param <S> the [source type][#getSourceType()].
/// @param <T> the [target type][#getTargetType()].
/// @since 1.0.0
public class BeanTypeMapper<S extends WritableBean, T> extends CompositeTypeMapper<S, T> {

  private final WritableProperty<T> property;

  private S template;

  /// The constructor.
  ///
  /// @param property the [WritableProperty] to map as segment.
  /// @param next the [next][#next()] [BeanTypeMapper]. May be `null`.
  /// @param template the [WritableBean] to use as template.
  public BeanTypeMapper(WritableProperty<T> property, BeanTypeMapper<S, ?> next, S template) {

    super(property.getName(), next);
    this.property = property;
    this.template = template;
  }

  @SuppressWarnings("unchecked")
  @Override
  public Class<? extends S> getSourceType() {

    return (Class<? extends S>) this.template.getType().getJavaClass();
  }

  @Override
  public Class<? extends T> getTargetType() {

    return this.property.getValueClass();
  }

  @Override
  public T toTarget(S source) {

    return source.get(this.property.getName());
  }

  @Override
  public Builder<S> sourceBuilder() {

    return BeanBuilder.ofTemplate(this.template);
  }

  @Override
  public void with(Builder<S> builder, T targetSegment) {

    builder.build().setDynamic(this.suffix, targetSegment);
  }

  /// @param <B> type of the [WritableBean].
  /// @param bean the [WritableBean] instance.
  /// @return the [BeanTypeMapper] to map the given [WritableBean].
  public static <B extends WritableBean> BeanTypeMapper<B, ?> of(B bean) {

    return of(bean, bean.getProperties().iterator());
  }

  private static <B extends WritableBean> BeanTypeMapper<B, ?> of(B bean,
      Iterator<? extends WritableProperty<?>> propertyIterator) {

    if (propertyIterator.hasNext()) {
      WritableProperty<?> property = propertyIterator.next();
      BeanTypeMapper<B, ?> next = of(bean, propertyIterator);
      BeanTypeMapper<B, ?> mapper = new BeanTypeMapper<>(property, next, bean);
      return mapper;
    }
    return null;
  }

}
