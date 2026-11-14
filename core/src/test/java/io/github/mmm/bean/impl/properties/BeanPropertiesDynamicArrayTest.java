package io.github.mmm.bean.impl.properties;

/// Test of [BeanPropertiesDynamicArray].
class BeanPropertiesDynamicArrayTest extends BeanPropertiesTest {
  @Override
  protected BeanProperties create() {

    return new BeanPropertiesDynamicArray(16);
  }

}
