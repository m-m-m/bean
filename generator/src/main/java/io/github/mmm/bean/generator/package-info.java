/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package io.github.mmm.bean.generator;
/**
 * Provides a generator for all {@link WritableBean bean interfaces} on classpath alongside with
 * {@link io.github.mmm.bean.BeanFactory} to create instances of these beans. Allows ahead-of-time (AOT) compilation for
 * environments where reflection is not available (GraalVM, TeaVM, etc.).
 */
