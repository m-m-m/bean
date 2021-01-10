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
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.ArrayList;
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

  /**
   * Scans the class-path of the given {@link ClassLoader} for interfaces extending {@link WritableBean} (do not use
   * module path when calling this).
   *
   * @param targetDir the {@link Path} to the base-directory where to generate the source code. Sub-directories for
   *        required packages will be created automatically.
   * @param classloader the {@link ClassLoader} to scan for interfaces extending {@link WritableBean}.
   */
  public void generate(Path targetDir, ClassLoader classloader) {

    List<BeanMetadata> metadataList = new ArrayList<>();
    try (BeanScanner scanner = new BeanScanner(classloader)) {
      Collection<Class<? extends WritableBean>> beanClasses = scanner.findBeanInterfaces();
      for (Class<? extends WritableBean> beanClass : beanClasses) {
        BeanMetadata metadata = generate(beanClass, targetDir);
        if (metadata != null) {
          metadataList.add(metadata);
        }
      }
      beanClasses = scanner.findBeanClasses();
      for (Class<? extends WritableBean> beanClass : beanClasses) {
        BeanMetadata metadata = BeanClassMetadata.of(beanClass);
        if (metadata != null) {
          metadataList.add(metadata);
        }
      }
    }
    generateFactory(metadataList, targetDir);
  }

  /**
   * @param beanClasses the {@link Collection} of {@link Class}es reflecting the {@link WritableBean}s to generate.
   * @param targetDir the {@link Path} to the base-directory where to generate the source code. Sub-directories for
   *        required packages will be created automatically.
   */
  public void generate(Collection<Class<? extends WritableBean>> beanClasses, Path targetDir) {

    List<BeanMetadata> metadataList = new ArrayList<>();
    for (Class<? extends WritableBean> beanClass : beanClasses) {
      BeanMetadata metadata = generate(beanClass, targetDir);
      if (metadata == null) {
        metadata = BeanClassMetadata.of(beanClass);
      }
      if (metadata != null) {
        metadataList.add(metadata);
      }
    }
    generateFactory(metadataList, targetDir);
  }

  /**
   * Generates the implementation of {@link BeanFactory} capable to {@link BeanFactory#create(Class) create} all
   * instances of the given {@link WritableBean} classes.
   *
   * @param metadatas the {@link Collection} with the {@link BeanMetadata} instances of the {@link WritableBean}s to be
   *        able to {@link BeanFactory#create(Class) create}.
   * @param targetDir the {@link Path} to the base-directory where to generate the source code. Sub-directories for
   *        required packages will be created automatically.
   */
  public void generateFactory(Collection<BeanMetadata> metadatas, Path targetDir) {

    try {
      Path packageDir = targetDir.resolve(BASE_PACKAGE);
      Files.createDirectories(packageDir);
      Path targetFile = packageDir.resolve("BeanFactoryImpl.java");
      try (BufferedWriter writer = Files.newBufferedWriter(targetFile, StandardCharsets.UTF_8)) {
        generateFactory(metadatas, writer);
      }
    } catch (IOException e) {
      throw new RuntimeIoException(e);
    }
  }

  /**
   * Generates the implementation of {@link BeanFactory} capable to {@link BeanFactory#create(Class) create} all
   * instances of the given {@link WritableBean} classes.
   *
   * @param metadatas the {@link Collection} with the {@link BeanMetadata} instances of the {@link WritableBean}s to be
   *        able to {@link BeanFactory#create(Class) create}.
   * @param writer the {@link Writer} to write the generated class to.
   */
  public void generateFactory(Collection<BeanMetadata> metadatas, Writer writer) {

    try {
      writePackageDeclaration(writer, BASE_PACKAGE);
      writeImportClasses(writer, List.of(AbstractBeanFactory.class));
      writeClassDeclaration(writer, "BeanFactoryImpl", AbstractBeanFactory.class.getSimpleName(), null);
      writer.write("  public BeanFactoryImpl() {\n");
      writer.write("    super();\n");
      for (BeanMetadata metadata : metadatas) {
        writer.write("    add(");
        writer.write(metadata.getBeanType().getName());
        writer.write(".class, x -> ");
        metadata.writeInstantiation(writer, "x");
        writer.write(");\n");
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
   * @return the {@link BeanInterfaceMetadata} created for the {@link WritableBean}.
   */
  public BeanInterfaceMetadata generate(Class<? extends WritableBean> beanClass, Path targetDir) {

    if (!BeanInterfaceMetadata.isNonAbstractInterface(beanClass)) {
      LOG.debug("Class is not an instantiable bean interface: {}", beanClass);
      return null;
    }
    LOG.debug("Generating implementation for {}", beanClass);
    try {
      String packageName = beanClass.getPackageName();
      Path packageDir = targetDir.resolve(BASE_PACKAGE + "/" + packageName.replace('.', '/'));
      Files.createDirectories(packageDir);
      Path targetFile = packageDir.resolve(beanClass.getSimpleName() + "Impl.java");
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
   * @return the {@link BeanInterfaceMetadata} created for the {@link WritableBean}.
   */
  public BeanInterfaceMetadata generate(Class<? extends WritableBean> beanClass, Writer writer) {

    try {
      BeanInterfaceMetadata metadata = BeanInterfaceMetadata.of(beanClass);
      if (metadata != null) {
        metadata.write(writer);
      }
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

    writeImports(writer, importTypes.stream().map(type -> type.getName()).collect(Collectors.toList()));
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

  /**
   * @param args the commandline arguments. The first argument is the target directory where the generated code is
   *        written to. If no arguments are provided, code is written to "./target/generated".
   */
  public static void main(String[] args) {

    Path targetDir;
    if (args.length > 0) {
      targetDir = Paths.get(args[0]);
    } else {
      targetDir = Paths.get("target", "generated");
    }
    BeanGenerator generator = new BeanGenerator();
    generator.generate(targetDir, null);
  }

}
