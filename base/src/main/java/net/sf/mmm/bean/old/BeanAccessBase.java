/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean.old;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import net.sf.mmm.bean.impl.BeanFactoryImpl;
import net.sf.mmm.marshall.StructuredReader;
import net.sf.mmm.marshall.StructuredWriter;
import net.sf.mmm.property.WritableProperty;
import net.sf.mmm.util.exception.api.ReadOnlyException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This is the abstract base implementation of {@link BeanAccess}.
 *
 * @param <BEAN> the generic type of the intercepted {@link #getBean() bean}.
 *
 * @author hohwille
 * @since 1.0.0
 */
public abstract class BeanAccessBase<BEAN extends MagicBean>
    implements InvocationHandler, BeanAccess, Iterable<WritableProperty<?>> {

  static final Logger LOG = LoggerFactory.getLogger(BeanAccessBase.class);

  private final Class<BEAN> beanClass;

  private final BEAN bean;

  private final BeanFactoryImpl beanFactory;

  /**
   * The constructor.
   *
   * @param beanClass - see {@link #getBeanClass()}.
   * @param beanFactory the owning {@link BeanFactoryImpl}.
   */
  public BeanAccessBase(Class<BEAN> beanClass, BeanFactoryImpl beanFactory) {

    this(beanClass, beanFactory, beanClass);
  }

  /**
   * The constructor.
   *
   * @param beanClass - see {@link #getBeanClass()}.
   * @param beanFactory the owning {@link BeanFactoryImpl}.
   * @param interfaces an array with optional {@link MagicBean} interfaces to be implemented by the dynamic proxy.
   */
  public BeanAccessBase(Class<BEAN> beanClass, BeanFactoryImpl beanFactory, Class<?>... interfaces) {

    super();
    this.beanClass = beanClass;
    this.bean = beanFactory.createProxy(this, interfaces);
    this.beanFactory = beanFactory;
  }

  @Override
  public Class<BEAN> getBeanClass() {

    return this.beanClass;
  }

  /**
   * @return the {@link MagicBean} proxy instance to {@link #invoke(Object, Method, Object[]) intercept}.
   */
  public BEAN getBean() {

    return this.bean;
  }

  /**
   * @return the {@link BeanAccessPrototype}.
   */
  protected abstract BeanAccessPrototype<BEAN> getPrototype();

  @Override
  public Iterable<WritableProperty<?>> getProperties() {

    return this;
  }

  /**
   * Gets the {@link WritableProperty} for the given {@code index}.
   *
   * @param prototypeProperty is the {@link BeanClassProperty}.
   * @param create - {@code true} if the property is required and shall be created if it {@link #isDynamic() does not
   *        already exist}, {@code false} otherwise.
   * @return the requested {@link WritableProperty}. May be {@code null}.
   */
  protected abstract WritableProperty<?> getProperty(BeanClassProperty prototypeProperty, boolean create);

  @Override
  public boolean isReadOnly() {

    return false;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    Object result = null;
    BeanPrototypeOperation operation = getPrototype().getOperation(method);
    if (operation != null) {
      result = operation.invoke(this, args);
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  static <BEAN extends MagicBean> BeanAccessBase<BEAN> get(BEAN bean) {

    return (BeanAccessBase<BEAN>) bean.access();
  }

  /**
   * @param propertyName the optional property name or {@code null} for write on the entire {@link MagicBean} itself.
   * @throws ReadOnlyException if this {@link MagicBean} is {@link #isReadOnly() read-only}.
   */
  protected void requireWritable(String propertyName) throws ReadOnlyException {

    if (isReadOnly()) {
      throw new ReadOnlyException(getBeanClass().getSimpleName(), propertyName);
    }
  }

  @Override
  public void write(StructuredWriter writer) {

    // TODO only write type if polymorphic...
    writer.writeName(PROPERTY_TYPE);
    writer.writeValueAsString(getSimpleName());
    for (WritableProperty<?> property : getProperties()) {
      property.write(writer);
    }
  }

  @Override
  public void read(StructuredReader reader) {

    requireWritable(null);
    while (!reader.readEnd()) {
      String propertyName = reader.readName();
      if (PROPERTY_TYPE.equals(propertyName)) {
        String type = reader.readValueAsString();
        if (!type.equals(getSimpleName())) {
          throw new IllegalStateException(PROPERTY_TYPE + "=" + type + "!=" + getSimpleName());
        }
      } else {
        readProperty(reader, propertyName);
      }

    }
  }

  protected void readProperty(StructuredReader reader, String propertyName) {

    WritableProperty<?> property = getProperty(propertyName);
    if (property == null) {
      readUndefinedProperty(reader, propertyName);
    } else {
      property.read(reader);
    }
  }

  /**
   * Called from {@link #readProperty(StructuredReader, String)} to parse a property that does not (yet) exist.
   *
   * @param reader the {@link StructuredReader}.
   * @param propertyName the name of the missing property to parse.
   */
  @SuppressWarnings({ "unchecked", "rawtypes" })
  protected void readUndefinedProperty(StructuredReader reader, String propertyName) {

    if (!isDynamic()) {
      LOG.debug("ignoring undefined property {}.{}", getBeanClass(), propertyName);
      reader.skipValue();
      return;
    }
    Object value = reader.readValue(true);
    if (value == null) {
      return; // ignore...
    }
    Class<? extends Object> valueClass = value.getClass();
    WritableProperty property = createProperty(propertyName, valueClass);
    property.setValue(value);
  }
}
