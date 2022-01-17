/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.generator;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;

import io.github.mmm.property.factory.PropertyFactory;
import io.github.mmm.property.factory.PropertyFactoryManager;

/**
 * Container for a {@link BeanPropertyContainer}.
 *
 * @since 1.0.0
 */
public class BeanPropertyContainer {

  private final String name;

  private BeanMethodContainerProperty propertyMethod;

  private BeanMethodContainerGetter getterMethod;

  private BeanMethodContainerSetter setterMethod;

  private Class<?> type;

  private String propertyType;

  /**
   * The constructor.
   *
   * @param name the {@link #getName() Name}.
   */
  public BeanPropertyContainer(String name) {

    super();
    this.name = name;
  }

  /**
   * @return the name of the property.
   */
  public String getName() {

    return this.name;
  }

  /**
   * @return propertyMethod
   */
  public BeanMethodContainerProperty getPropertyMethod() {

    return this.propertyMethod;
  }

  /**
   * @return getterMethod
   */
  public BeanMethodContainerGetter getGetterMethod() {

    return this.getterMethod;
  }

  /**
   * @return setterMethod
   */
  public BeanMethodContainerSetter getSetterMethod() {

    return this.setterMethod;
  }

  /**
   * @param method the {@link BeanMethodContainer} to register in this {@link BeanPropertyContainer}.
   */
  public void add(BeanMethodContainer method) {

    if (method instanceof BeanMethodContainerProperty) {
      this.propertyMethod = specialize(this.propertyMethod, (BeanMethodContainerProperty) method);
    } else if (method instanceof BeanMethodContainerGetter) {
      this.getterMethod = specialize(this.getterMethod, (BeanMethodContainerGetter) method);
    } else if (method instanceof BeanMethodContainerSetter) {
      this.setterMethod = specialize(this.setterMethod, (BeanMethodContainerSetter) method);
    }
  }

  private <M extends BeanMethodContainer> M specialize(M existing, M update) {

    if (existing != null) {
      if (!update.isSpecialized(existing)) {
        return existing;
      }
    }
    return update;
  }

  /**
   * @return type
   */
  public Class<?> getType() {

    if (this.type == null) {
      this.type = getType(this.propertyMethod);
      if (this.type == null) {
        this.type = getType(this.getterMethod);
        if (this.type == null) {
          this.type = getType(this.setterMethod);
          if (this.type == null) {
            throw new IllegalStateException();
          }
        }
      }
    }
    return this.type;
  }

  /**
   * @return propertyType
   */
  public String getPropertyType() {

    if (this.propertyType == null) {
      if (this.propertyMethod != null) {
        this.propertyType = this.propertyMethod.getMethod().getReturnType().getSimpleName();
      } else {
        PropertyFactory<?, ?> factory = PropertyFactoryManager.get().getFactoryForValueType(getType());
        if (factory != null) {
          this.propertyType = factory.getImplementationClass().getName();
        }
      }

    }
    return this.propertyType;
  }

  private Class<?> getType(BeanMethodContainer method) {

    if (method != null) {
      return method.getPropertyType();
    }
    return null;
  }

  /**
   * Writes the property field declaration.
   *
   * @param writer the {@link Writer}.
   * @throws IOException on error.
   */
  public void writeField(Writer writer) throws IOException {

    writer.write("  private final ");
    if (getPropertyType() == null) {
      System.out.println("error");
    }
    writer.write(getPropertyType());
    writer.write(" ");
    writer.write(this.name);
    writer.write(";\n");
  }

  /**
   * Writes the property field initializer statement for constructor.
   *
   * @param writer the {@link Writer}.
   * @throws IOException on error.
   */
  public void writeFieldInitializer(Writer writer) throws IOException {

    writer.write("    this.");
    writer.write(this.name);
    writer.write(" = add(");
    if ((this.propertyMethod != null) && this.propertyMethod.getMethod().isDefault()) {
      Method method = this.propertyMethod.getMethod();
      writer.write(method.getDeclaringClass().getSimpleName());
      writer.write(".super.");
      writer.write(method.getName());
      writer.write("()");
    } else {
      boolean standardType = BeanGenerator.STANDARD_TYPES.contains(getType());
      if (standardType) {
        writer.write(").new");
        writer.write(getType().getSimpleName());
        writer.write("().");
        // validator here...
        writer.write("build(\"");
        writer.write(this.name);
        writer.write("\"");
      } else {
        writer.write("new ");
        writer.write(getPropertyType());
        writer.write("(\"");
        writer.write(this.name);
        writer.write("\")");
      }
    }
    writer.write(");\n");
  }

  /**
   * Writes the methods.
   *
   * @param writer the {@link Writer}.
   * @throws IOException on error.
   */
  public void writeMethods(Writer writer) throws IOException {

    if (this.propertyMethod != null) {
      this.propertyMethod.write(writer);
    }
    if (this.getterMethod != null) {
      this.getterMethod.write(writer);
    }
    if (this.setterMethod != null) {
      this.setterMethod.write(writer);
    }
  }

  @Override
  public String toString() {

    return this.name;
  }

}
