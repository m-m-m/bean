package io.github.mmm.bean.property;

import java.util.Iterator;

import io.github.mmm.base.lang.Builder;
import io.github.mmm.bean.BeanBuilder;
import io.github.mmm.bean.WritableBean;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.value.converter.CompositeTypeMapper;

/**
 * {@link CompositeTypeMapper} for a {@link WritableBean}.
 *
 * @param <S> the {@link #getSourceType() source type}.
 * @param <T> the {@link #getTargetType() target type}.
 * @since 1.0.0
 */
public class BeanTypeMapper<S extends WritableBean, T> extends CompositeTypeMapper<S, T> {

  private final WritableProperty<T> property;

  private S template;

  /**
   * The constructor.
   *
   * @param property the {@link WritableProperty} to map as segment.
   * @param next the {@link #next() next} {@link BeanTypeMapper}. May be {@code null}.
   * @param template the {@link WritableBean} to use as template.
   */
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

  /**
   * @param <B> type of the {@link WritableBean}.
   * @param bean the {@link WritableBean} instance.
   * @return the {@link BeanTypeMapper} to map the given {@link WritableBean}.
   */
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
