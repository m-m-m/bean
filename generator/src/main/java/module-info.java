/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * Provides a generator for all {@link WritableBean bean interfaces} on classpath alongside with
 * {@link io.github.mmm.bean.BeanFactory} to create instances of these beans. Allows ahead-of-time (AOT) compilation for
 * environments where reflection is not available (GraalVM, TeaVM, etc.).
 */
module io.github.mmm.bean.generator {

  requires transitive io.github.mmm.bean;

  requires io.github.mmm.bean.factory;

  requires io.github.classgraph;

  // provides BeanFactory with BeanFactoryImpl;

  exports io.github.mmm.bean.generator;

}
