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
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.exception.RuntimeIoException;
import io.github.mmm.bean.AbstractBeanFactory;
import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.WritableBean;

/**
 * Code generator to create implementations of {@link WritableBean} sub-interfaces and {@link BeanFactory} Java
 * source-code.
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

  /**
   * @param beanClasses the {@link Collection} of {@link Class}es reflecting the {@link WritableBean}s to generate.
   * @param targetDir the {@link Path} to the base-directory where to generate the source-code. Sub-directories for
   *        required packages will be created automatically.
   */
  public void generate(Collection<Class<? extends WritableBean>> beanClasses, Path targetDir) {

    for (Class<? extends WritableBean> beanClass : beanClasses) {
      generate(beanClass, targetDir);
    }
  }

  public void generateFactory(Collection<Class<? extends WritableBean>> beanClasses, Path targetDir) {

    try {
      Path packageDir = targetDir.resolve(BASE_PACKAGE);
      Files.createDirectories(packageDir);
      Path targetFile = packageDir.resolve("BeanFactoryImpl.java");
      try (BufferedWriter writer = Files.newBufferedWriter(targetFile, StandardCharsets.UTF_8)) {
        generateFactory(beanClasses, writer);
      }
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  public void generateFactory(Collection<Class<? extends WritableBean>> beanClasses, Writer writer) {

    try {
      writePackageDeclaration(writer, BASE_PACKAGE);
      writeImportClasses(writer, List.of(AbstractBeanFactory.class));
      writeClassDeclaration(writer, "BeanFactoryImpl", AbstractBeanFactory.class.getSimpleName(), null);
      writer.write("  public BeanFactoryImpl() {\n");
      writer.write("    super();\n");
      for (Class<? extends WritableBean> beanClass : beanClasses) {
        writer.write("    add(");
        writer.write(beanClass.getName());
        writer.write(".class, x -> new ");
        writer.write(BASE_PACKAGE);
        writer.write(".");
        writer.write(beanClass.getName());
        writer.write("Impl(x));\n");
      }
      writer.write("  }\n");
      writer.write("}\n");
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  /**
   * @param beanClass the {@link Class} reflecting the {@link WritableBean} to generate.
   * @param targetDir the {@link Path} to the base-directory where to generate the source-code. Sub-directories for
   *        required packages will be created automatically.
   * @return the {@link BeanMetadata} created for the {@link WritableBean}.
   */
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

  /**
   * @param beanClass the {@link Class} reflecting the {@link WritableBean} to generate.
   * @param writer the {@link Writer} to write the Java source code to.
   * @return the {@link BeanMetadata} created for the {@link WritableBean}.
   */
  public BeanMetadata generate(Class<? extends WritableBean> beanClass, Writer writer) {

    try {
      BeanMetadata metadata = new BeanMetadata(beanClass);
      metadata.write(writer);
      return metadata;
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  /**
   * Writes the package declaration.
   *
   * @param writer the {@link Writer}.
   * @param packageName the {@link Package#getName() package name}.
   * @throws IOException on error.
   */
  public static void writePackageDeclaration(Writer writer, String packageName) throws IOException {

    writer.write("package ");
    writer.write(packageName);
    writer.write(";\n");
  }

  /**
   * Writes the import statements.
   *
   * @param writer the {@link Writer}.
   * @param importTypes the {@link Collection} of qualified imports.
   * @throws IOException on error.
   */
  public static void writeImportClasses(Writer writer, Collection<Class<?>> importTypes) throws IOException {

    writeImports(writer, importTypes.stream().map(type -> type.getSimpleName()).collect(Collectors.toList()));
  }

  /**
   * Writes the import statements.
   *
   * @param writer the {@link Writer}.
   * @param importTypes the {@link Collection} of qualified imports.
   * @throws IOException on error.
   */
  public static void writeImports(Writer writer, Collection<String> importTypes) throws IOException {

    writer.write("\n");
    for (String importType : importTypes) {
      writer.write("import ");
      writer.write(importType);
      writer.write(";\n");
    }
    if (!importTypes.isEmpty()) {
      writer.write("\n");
    }
  }

  /**
   * Writes the class declaration.
   *
   * @param writer the {@link Writer}.
   * @param className the {@link Class#getSimpleName() simple class name}.
   * @param superClass the optional {@link Class#getSuperclass() super class}.
   * @param superInterface the optional {@link Class#getInterfaces() interface}.
   * @throws IOException on error.
   */
  public static void writeClassDeclaration(Writer writer, String className, String superClass, String superInterface)
      throws IOException {

    writer.write("public class ");
    writer.write(className);
    if (superClass != null) {
      writer.write(" extends ");
      writer.write(superClass);
    }
    if (superInterface != null) {
      writer.write(" implements ");
      writer.write(superInterface);
    }
    writer.write(" {\n");
    writer.write("\n");
  }

}
