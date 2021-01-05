/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.generator;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.exception.RuntimeIoException;
import io.github.mmm.bean.WritableBean;

/**
 *
 */
public class BeanGenerator {

  /** The base package where the generated code is located in. */
  public static final String BASE_PACKAGE = "beanimpl";

  static final Set<Class<?>> STANDARD_TYPES = Set.of(String.class, Boolean.class, Integer.class, Long.class,
      Double.class, Float.class, Short.class, Byte.class, BigDecimal.class, BigInteger.class, Instant.class,
      LocalDate.class, LocalDateTime.class, OffsetTime.class, OffsetDateTime.class, ZonedDateTime.class);

  private static final Logger LOG = LoggerFactory.getLogger(BeanGenerator.class);

  public void generate(Path targetDir, ClassLoader classLoader) {

  }

  public void generate(Collection<Class<? extends WritableBean>> beanClasses, Path targetDir) {

    for (Class<? extends WritableBean> beanClass : beanClasses) {
      generate(beanClass, targetDir);
    }
  }

  public BeanMetadata generate(Class<? extends WritableBean> beanClass, Path targetDir) {

    LOG.debug("Generating implementation for {}", beanClass);
    try {
      String packageName = beanClass.getPackageName();
      Path packageDir = targetDir.resolve(BASE_PACKAGE + "/" + packageName.replace('.', '/'));
      Files.createDirectories(packageDir);
      Path targetFile = packageDir.resolve(beanClass.getSimpleName() + ".java");
      try (BufferedWriter writer = Files.newBufferedWriter(targetFile, StandardCharsets.UTF_8)) {
        return generate(beanClass, writer);
      }
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  public BeanMetadata generate(Class<? extends WritableBean> beanClass, Writer writer) {

    try {
      BeanMetadata metadata = new BeanMetadata(beanClass);
      metadata.write(writer);
      return metadata;
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

}
