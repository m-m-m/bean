package io.github.mmm.bean;

import io.github.mmm.base.lang.Builder;

/// Implementation of [Builder] for a [WritableBean].
///
/// @param <B> type of the [WritableBean].
/// @since 1.0.0
public class BeanBuilder<B extends WritableBean> implements Builder<B> {

  private final B bean;

  /// The constructor.
  ///
  /// @param bean the [WritableBean] to "[build][#build()]" (that is actually already there).
  public BeanBuilder(B bean) {

    super();
    this.bean = bean;
  }

  @Override
  public B build() {

    return this.bean;
  }

  /// @param <B> type of the [WritableBean].
  /// @param bean the [WritableBean] to "[build][#build()]" (that is actually already there).
  /// @return the [BeanBuilder] instance.
  public static <B extends WritableBean> BeanBuilder<B> of(B bean) {

    return new BeanBuilder<>(bean);
  }

  /// @param <B> type of the [WritableBean].
  /// @param beanTemplate the [WritableBean] to use as template to [create a new instance
  ///        from][ReadableBean#newInstance()].
  /// @return the [BeanBuilder] instance.
  public static <B extends WritableBean> BeanBuilder<B> ofTemplate(B beanTemplate) {

    B bean = ReadableBean.newInstance(beanTemplate);
    return of(bean);
  }
}