package io.github.mmm.bean.impl.properties;

/**
 * Test of {@link BeanPropertiesDynamicArray}.
 */
class BeanPropertiesDynamicArrayTest extends BeanPropertiesTest {
  @Override
  protected BeanProperties create() {

    return new BeanPropertiesDynamicArray(16);
  }

}
