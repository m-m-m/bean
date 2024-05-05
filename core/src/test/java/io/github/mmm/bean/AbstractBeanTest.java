/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import org.assertj.core.api.Assertions;

import io.github.mmm.bean.impl.BeanTypeImpl;
import io.github.mmm.bean.property.BeanProperty;
import io.github.mmm.property.WritableProperty;
import io.github.mmm.value.observable.ObservableEventReceiver;

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
      stableName = BeanTypeImpl.getStableName(javaClass);
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
   * @param newValue a new {@link WritableProperty#get() value} different from the current property value.
   */
  protected <V> void checkProperty(AbstractBean bean, WritableProperty<V> property, V newValue) {

    String propertyName = property.getName();
    assertThat(bean.getRequiredProperty(propertyName)).isSameAs(property);
    if (!bean.isReadOnly()) {
      WritableBean readOnlyBean = bean.getReadOnly();
      assertThat(readOnlyBean.isReadOnly()).isTrue();
      assertThat(readOnlyBean.getClass()).isSameAs(bean.getClass());
      WritableProperty<?> readOnlyProperty = readOnlyBean.getRequiredProperty(property.getName());
      assertThat(readOnlyProperty).isNotSameAs(property);
      assertThat(readOnlyProperty.isReadOnly()).isTrue();
      if (!(property instanceof BeanProperty)) {
        assertThat(readOnlyProperty).isEqualTo(property);
        assertThat(property.get()).isEqualTo(bean.get(property.getName()))
            .isEqualTo(readOnlyBean.get(property.getName()));
      }
      if (newValue != null) {
        assertThat(property.get()).isNotEqualTo(newValue);
        ObservableEventReceiver<Object> listener = new ObservableEventReceiver<>();
        property.addListener(listener);
        property.set(newValue);
        assertThat(listener.getEventCount()).isEqualTo(1);
        assertThat(listener.getEvent().getValue()).isEqualTo(newValue);
        property.removeListener(listener);
      }
    }
  }

}
