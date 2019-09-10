/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.mapping;

import net.sf.mmm.bean.entity.EntityBean;
import net.sf.mmm.bean.old.BeanFactory;
import net.sf.mmm.bean.old.MagicBean;

/**
 * This is the interface used to map a fixed type of <em>document</em> (generic container object) {@link #toBean(Object)
 * to} and {@link #fromBean(MagicBean) from} instances of {@link MagicBean}.
 *
 * @param <D> the type of the document (generic container object) to map (e.g. a document from a CMS or NoSQL-database).
 * @param <B> the base type of the {@link MagicBean} to map (e.g. {@link MagicBean} or {@link EntityBean}).
 *
 * @author hohwille
 * @since 1.0.0
 */
public interface DocumentBeanMapper<D, B extends MagicBean> {

  /**
   * @param <T> the generic type of the {@link MagicBean} to map to.
   * @param document the generic container object to map.
   * @return a corresponding {@link MagicBean}-{@link BeanFactory#create(MagicBean) instance} with the properties mapped from the
   *         given container object.
   */
  <T extends B> T toBean(D document);

  /**
   * @param bean the {@link MagicBean} to map.
   * @return the a {@link Class#newInstance() new instance} of the document (generic container object) with the
   *         properties mapped from the given {@link MagicBean}.
   */
  D fromBean(B bean);

}
