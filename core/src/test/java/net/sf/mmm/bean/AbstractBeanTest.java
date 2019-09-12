/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package net.sf.mmm.bean;

import net.sf.mmm.bean.impl.BeanTypeImpl;
import net.sf.mmm.property.WritableProperty;
import net.sf.mmm.value.observable.ObservableEventReceiver;

import org.assertj.core.api.Assertions;

/**
 * Test of {@link DynamicBean}.
 */
public abstract class AbstractBeanTest extends Assertions {

  /**
   * @param bean the {@link AbstractBean}.
   * @param stableName the {@link BeanType#getStableName() stable name} or {@code null} to determine.
   */
  protected void checkType(AbstractBean bean, String stableName) {

    BeanType type = bean.getType();
    Class<? extends AbstractBean> javaClass = bean.getClass();
    assertThat(type.getJavaClass()).isSameAs(javaClass);
    assertThat(type.getPackageName()).isEqualTo(javaClass.getPackageName());
    assertThat(type.getSimpleName()).isEqualTo(javaClass.getSimpleName());
    assertThat(type.getQualifiedName()).isEqualTo(javaClass.getName());
    if (stableName == null) {
      stableName = BeanTypeImpl.getStableName(javaClass, stableName);
    }
    assertThat(type.getStableName()).isEqualTo(stableName);
    assertThat(type.toString()).isEqualTo(javaClass.getName());
  }

  /**
   * @param bean the {@link AdvancedBean} to check.
   * @param property the {@link AdvancedBean#getRequiredProperty(String) required property} supposed to exist in the
   *        given bean.
   */
  protected void checkProperty(AbstractBean bean, WritableProperty<?> property) {

    checkProperty(bean, property, null);
  }

  /**
   * @param <V> type of the value.
   * @param bean the {@link AdvancedBean} to check.
   * @param property the {@link AdvancedBean#getRequiredProperty(String) required property} supposed to exist in the
   *        given bean.
   * @param newValue a new {@link WritableProperty#getValue() value} different from the current property value.
   */
  protected <V> void checkProperty(AbstractBean bean, WritableProperty<V> property, V newValue) {

    String propertyName = property.getName();
    assertThat(bean.getRequiredProperty(propertyName)).isSameAs(property);
    if (!bean.isReadOnly()) {
      AbstractBean readOnlyBean = bean.getReadOnly();
      assertThat(readOnlyBean.isReadOnly()).isTrue();
      assertThat(readOnlyBean.getClass()).isSameAs(bean.getClass());
      WritableProperty<?> readOnlyProperty = readOnlyBean.getRequiredProperty(property.getName());
      assertThat(readOnlyProperty).isNotSameAs(property).isEqualTo(property).isSameAs(property.getReadOnly());
      assertThat(readOnlyProperty.isReadOnly()).isTrue();
      assertThat(property.getValue()).isEqualTo(bean.get(property.getName()))
          .isEqualTo(readOnlyBean.get(property.getName()));
      if (newValue != null) {
        assertThat(property.getValue()).isNotEqualTo(newValue);
        ObservableEventReceiver<Object> listener = new ObservableEventReceiver<>();
        readOnlyProperty.addListener(listener);
        property.setValue(newValue);
        assertThat(listener.getEventCount()).isEqualTo(1);
        assertThat(listener.getEvent().getValue()).isEqualTo(newValue);
        readOnlyProperty.removeListener(listener);
      }
    }
  }

}
