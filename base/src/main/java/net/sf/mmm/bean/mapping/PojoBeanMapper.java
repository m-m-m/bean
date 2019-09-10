/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.mapping;

import net.sf.mmm.bean.entity.EntityBean;
import net.sf.mmm.bean.old.BeanFactory;
import net.sf.mmm.bean.old.MagicBean;

/**
 * This is the interface used to map instances from a hierarchy of {@link net.sf.mmm.util.pojo.api.Pojo POJOs}
 * {@link #toBean(Object, MagicBean) to} amd {@link #fromBean(MagicBean, Class) from} a corresponding hierarchy of {@link MagicBean}s.
 * E.g. it will be used to map from {@code FooEntity} to {@code FooBean} or from {@code BarBean} to {@code BarEntity}.
 * To map from a fixed generic container object see {@link DocumentBeanMapper} instead.
 *
 * @param <P> the base type of the {@link net.sf.mmm.util.pojo.api.Pojo}s to convert (e.g. {@link Object} or
 *        {@link net.sf.mmm.util.data.api.entity.Entity}).
 * @param <B> the base type of the {@link MagicBean} to map (e.g. {@link MagicBean} or {@link EntityBean}).
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface PojoBeanMapper<P, B extends MagicBean> {

  /**
   * @param <T> the generic type of the {@link MagicBean} to map to.
   * @param pojo the {@link net.sf.mmm.util.pojo.api.Pojo} to map.
   * @param prototype the {@link MagicBean} {@link BeanFactory#createPrototype(Class) prototype}.
   * @return a {@link MagicBean}-{@link BeanFactory#create(MagicBean) instance} of the given
   *         {@link BeanFactory#createPrototype(Class) prototype} with the properties mapped from the given
   *         {@link net.sf.mmm.util.pojo.api.Pojo}.
   */
  <T extends B> T toBean(P pojo, T prototype);

  /**
   *
   * @param <T> the generic type of the {@link net.sf.mmm.util.pojo.api.Pojo} to map to.
   * @param bean the {@link MagicBean} to map.
   * @param type the {@link Class} reflecting the {@link net.sf.mmm.util.pojo.api.Pojo} to map to.
   * @return the a {@link Class#newInstance() new instance} of the given {@link Class} with the properties mapped from
   *         the given {@link MagicBean}.
   */
  <T extends P> T fromBean(B bean, Class<T> type);

}
