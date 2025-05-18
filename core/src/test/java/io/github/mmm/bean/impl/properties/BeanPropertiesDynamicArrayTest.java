package io.github.mmm.bean.impl.properties;

/**
 * Test of {@link BeanPropertiesDynamicArray}.
 */
public class BeanPropertiesDynamicArrayTest extends BeanPropertiesTest {
  @Override
  protected BeanProperties create() {

    return new BeanPropertiesDynamicArray(16);
  }

}
