/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.impl.mapping;

import java.util.ServiceLoader;

import io.github.mmm.base.config.ServiceHelper;
import io.github.mmm.bean.mapping.ClassNameMapper;

/**
 * Provider of {@link ClassNameMapper}.
 *
 * @since 1.0.0
 */
public class ClassNameMapperProvider {

  /** Instance of {@link ClassNameMapper}. */
  public static final ClassNameMapper MAPPER = ServiceHelper.singleton(ServiceLoader.load(ClassNameMapper.class));

}
