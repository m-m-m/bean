/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.generator;

import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Method;

import io.github.mmm.bean.BeanHelper;
import io.github.mmm.bean.PropertyMethod;
import io.github.mmm.bean.ReadableBean;

/**
 * Wrapper for a {@link Method} of a {@link io.github.mmm.bean.WritableBean}.
 *
 * @since 1.0.0
 */
public abstract class BeanMethod {

  /** @see #getMethod() */
  protected final Method method;

  /** @see #getPropertyName() */
  protected final String propertyName;

  private String parameterName;

  /**
   * The constructor.
   *
   * @param method the {@link #getMethod() method}.
   * @param propertyName the {@link #getPropertyName() property name}.
   */
  public BeanMethod(Method method, String propertyName) {

    super();
    this.method = method;
    this.propertyName = propertyName;
  }

  /**
   * Writes the method.
   *
   * @param writer the {@link Writer}.
   * @throws IOException on error.
   */
  public void write(Writer writer) throws IOException {

    writer.write("\n");
    writer.write("  @Override\n");
    writer.write("  public ");
    writer.write(this.method.getReturnType().getSimpleName());
    writer.write(" ");
    writer.write(this.method.getName());
    writer.write("(");
    writeParameters(writer);
    writer.write(") {\n");
    writeBody(writer);
    writer.write("  }\n");
  }

  /**
   * Writes the method parameters.
   *
   * @param writer the {@link Writer}.
   * @throws IOException on error.
   */
  protected void writeParameters(Writer writer) throws IOException {

  }

  /**
   * Writes the method body (implementation).
   *
   * @param writer the {@link Writer}.
   * @throws IOException on error.
   */
  protected abstract void writeBody(Writer writer) throws IOException;

  /**
   * @return the underlying {@link io.github.mmm.bean.WritableBean bean} {@link Method}.
   */
  public Method getMethod() {

    return this.method;
  }

  /**
   * @return property the name of the property handled by this method.
   */
  public String getPropertyName() {

    return this.propertyName;
  }

  /**
   * @return property the name of the property handled by this method.
   */
  public String getParameterName() {

    if (this.parameterName == null) {
      this.parameterName = Character.toLowerCase(this.propertyName.charAt(0)) + this.propertyName.substring(1);
    }
    return this.parameterName;
  }

  /**
   * @return the {@link Class} reflecting the type of the property value.
   */
  public abstract Class<?> getPropertyType();

  /**
   * @param method the {@link Method} to introspect.
   * @return the corresponding {@link BeanMethod} or {@code null} if not a {@link BeanMethod} (to implement).
   */
  public static BeanMethod of(Method method) {

    String methodName = method.getName();
    char first = methodName.charAt(0);
    boolean defaultMethod = method.isDefault();
    int parameterCount = method.getParameterCount();
    if (defaultMethod) {
      if ((parameterCount == 0) && method.isAnnotationPresent(PropertyMethod.class)) {
        return new BeanMethodProperty(method, methodName);
      }
    } else if (parameterCount == 0) {
      if (Character.isUpperCase(first)) {
        return new BeanMethodProperty(method, methodName);
      } else if (methodName.endsWith(ReadableBean.SUFFIX_PROPERTY)) {
        String propertyName = Character.toUpperCase(first)
            + methodName.substring(1, methodName.length() - ReadableBean.SUFFIX_PROPERTY.length());
        return new BeanMethodProperty(method, propertyName);
      } else {
        String propertyName = BeanHelper.getPropertyForGetter(methodName);
        if (propertyName != null) {
          return new BeanMethodGetter(method, propertyName);
        }
      }
    } else if (parameterCount == 1) {
      String propertyName = BeanHelper.getPropertyForSetter(methodName);
      if (propertyName != null) {
        return new BeanMethodSetter(method, propertyName);
      }
    }
    return null;
  }

  /**
   * @param other the {@link BeanMethod} to compare.
   * @return {@code true} if {@code this} {@link BeanMethod} is more specialized than the given one (e.g. it overrides
   *         the given method), {@code false} otherwise.
   */
  public boolean isSpecialized(BeanMethod other) {

    if (other.method.getDeclaringClass().isAssignableFrom(this.method.getDeclaringClass())) {
      return true;
    }
    return false;
  }

}
