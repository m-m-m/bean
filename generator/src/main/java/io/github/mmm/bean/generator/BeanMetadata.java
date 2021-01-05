/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.generator;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.bean.AbstractBean;
import io.github.mmm.bean.AdvancedBean;
import io.github.mmm.bean.Bean;
import io.github.mmm.bean.BeanClass;
import io.github.mmm.bean.VirtualBean;
import io.github.mmm.bean.WritableBean;

/**
 * Collector for introspection data of {@link WritableBean beans}.
 *
 * @since 1.0.0
 */
public class BeanMetadata {

  private static final Logger LOG = LoggerFactory.getLogger(BeanMetadata.class);

  private final Class<? extends WritableBean> beanType;

  private final Class<? extends AbstractBean> superClass;

  private final boolean virtual;

  private final Set<String> importTypes;

  private final Map<String, BeanProperty> properties;

  private final Set<Class<?>> typesVisited;

  /**
   * The constructor.
   *
   * @param beanClass primary bean type.
   */
  public BeanMetadata(Class<? extends WritableBean> beanClass) {

    super();
    this.beanType = beanClass;
    this.importTypes = new TreeSet<>();
    this.properties = new TreeMap<>();
    this.typesVisited = new HashSet<>();
    if (VirtualBean.class.isAssignableFrom(this.beanType)) {
      this.superClass = AdvancedBean.class;
      this.virtual = true;
      addImport(BeanClass.class);
    } else {
      this.superClass = Bean.class;
      this.virtual = false;
    }
    addImport(this.beanType);
    addImport(this.superClass);
    introspect(beanClass);
  }

  void addImport(Class<?> type) {

    if (type.isPrimitive() || type.getPackageName().equals("java.lang")) {
      return;
    }
    this.importTypes.add(type.getName());
  }

  void introspect(Class<?> type) {

    boolean added = this.typesVisited.add(type);
    if (!added) {
      LOG.trace("{}: Already visited type {}", this.beanType, type);
      return;
    }
    LOG.trace("{}: Introspecting type {}", this.beanType, type);
    if ((type == WritableBean.class) || (type == VirtualBean.class)) {
      return;
    }
    for (Method method : type.getDeclaredMethods()) {
      introspect(method);
    }
    for (Class<?> superInterface : type.getInterfaces()) {
      introspect(superInterface);
    }
  }

  private BeanProperty getProperty(String name) {

    return this.properties.computeIfAbsent(name, x -> new BeanProperty(name));
  }

  private void introspect(Method method) {

    BeanMethod beanMethod = BeanMethod.of(method);
    if (beanMethod != null) {
      String propertyName = beanMethod.getPropertyName();
      getProperty(propertyName).add(beanMethod);
      addImport(method.getReturnType());
      for (Parameter parameter : method.getParameters()) {
        addImport(parameter.getType());
      }
      if (method.isDefault()) {
        addImport(method.getDeclaringClass());
      }
    }
  }

  /**
   * Writes the entire Java file.
   *
   * @param writer the {@link Writer}.
   * @throws IOException on error.
   */
  public void write(Writer writer) throws IOException {

    writePackageDeclaration(writer);
    writeImports(writer);
    writeClassDeclaration(writer);
    writeBody(writer);
    writer.write("}\n");
  }

  /**
   * Writes the package declaration.
   *
   * @param writer the {@link Writer}.
   * @throws IOException on error.
   */
  public void writePackageDeclaration(Writer writer) throws IOException {

    BeanGenerator.writePackageDeclaration(writer, BeanGenerator.BASE_PACKAGE + "." + this.beanType.getPackageName());
  }

  /**
   * Writes the import statements.
   *
   * @param writer the {@link Writer}.
   * @throws IOException on error.
   */
  public void writeImports(Writer writer) throws IOException {

    BeanGenerator.writeImports(writer, this.importTypes);
  }

  /**
   * Writes the class declaration.
   *
   * @param writer the {@link Writer}.
   * @throws IOException on error.
   */
  public void writeClassDeclaration(Writer writer) throws IOException {

    writer.write("public class ");
    writer.write(this.beanType.getSimpleName() + "Impl");
    writer.write(" extends ");
    writer.write(this.superClass.getSimpleName());
    writer.write(" implements ");
    writer.write(this.beanType.getSimpleName());
    writer.write(" {\n");
    writer.write("\n");
  }

  /**
   * Writes the fields.
   *
   * @param writer the {@link Writer}.
   * @throws IOException on error.
   */
  public void writeFields(Writer writer) throws IOException {

    for (BeanProperty property : this.properties.values()) {
      property.writeField(writer);
    }
  }

  /**
   * Writes the constructor(s).
   *
   * @param writer the {@link Writer}.
   * @throws IOException on error.
   */
  public void writeConstructors(Writer writer) throws IOException {

    writer.write("\n");
    writer.write("  public ");
    writer.write(this.beanType.getSimpleName() + "Impl(");
    if (this.virtual) {
      writer.write("BeanClass type");
    }
    writer.write(") {\n");
    writer.write("    super(");
    if (this.virtual) {
      writer.write("type");
    }
    writer.write(");\n");
    for (BeanProperty property : this.properties.values()) {
      property.writeFieldInitializer(writer);
    }
    writer.write("  }\n");
  }

  /**
   * Writes the methods.
   *
   * @param writer the {@link Writer}.
   * @throws IOException on error.
   */
  public void writeMethods(Writer writer) throws IOException {

    for (BeanProperty property : this.properties.values()) {
      property.writeMethods(writer);
    }
    writer.write("\n");
    writer.write("  @Override\n");
    writer.write("  protected AbstractBean create() {\n");
    writer.write("    return new ");
    writer.write(this.beanType.getSimpleName());
    writer.write("Impl(");
    if (this.virtual) {
      writer.write("getType()");
    }
    writer.write(");\n");
    writer.write("  }\n");
  }

  /**
   * Writes the class body.
   *
   * @param writer the {@link Writer}.
   * @throws IOException on error.
   */
  public void writeBody(Writer writer) throws IOException {

    writeFields(writer);
    writeConstructors(writer);
    writeMethods(writer);
  }

}
