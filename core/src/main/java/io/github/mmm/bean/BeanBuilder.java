package io.github.mmm.bean;

import io.github.mmm.base.lang.Builder;

/**
 * Implementation of {@link Builder} for a {@link WritableBean}.
 *
 * @param <B> type of the {@link WritableBean}.
 * @since 1.0.0
 */
public class BeanBuilder<B extends WritableBean> implements Builder<B> {

  private final B bean;

  /**
   * The constructor.
   *
   * @param bean the {@link WritableBean} to "{@link #build() build}" (that is actually already there).
   */
  public BeanBuilder(B bean) {

    super();
    this.bean = bean;
  }

  @Override
  public B build() {

    return this.bean;
  }

  /**
   * @param <B> type of the {@link WritableBean}.
   * @param bean the {@link WritableBean} to "{@link #build() build}" (that is actually already there).
   * @return the {@link BeanBuilder} instance.
   */
  public static <B extends WritableBean> BeanBuilder<B> of(B bean) {

    return new BeanBuilder<>(bean);
  }

  /**
   * @param <B> type of the {@link WritableBean}.
   * @param beanTemplate the {@link WritableBean} to use as template to {@link ReadableBean#newInstance() create a new
   *        instance from}.
   * @return the {@link BeanBuilder} instance.
   */
  public static <B extends WritableBean> BeanBuilder<B> ofTemplate(B beanTemplate) {

    B bean = ReadableBean.newInstance(beanTemplate);
    return of(bean);
  }
}