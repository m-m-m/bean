/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.bean.examples.TestBean;
import io.github.mmm.marshall.MarshallingConfig;
import io.github.mmm.marshall.StandardFormat;
import io.github.mmm.marshall.StructuredTextFormat;

/**
 * Test of marshalling and unmarshalling {@link io.github.mmm.bean.Bean} via {@link TestBean}.
 */
class BeanMarshallingTest extends Assertions {

  private static final int AGE = 42;

  private static final String NAME = "John Doe";

  private static final String JSON = "{\"Age\":42,\"Name\":\"John Doe\"}";

  /**
   * Test of marshalling {@link TestBean} to JSON.
   */
  @Test
  void testMarshallToJson() {

    // arrange
    TestBean bean = new TestBean();
    bean.Name.set(NAME);
    bean.Age.setValue(AGE);
    StructuredTextFormat jsonFormat = StandardFormat.json(MarshallingConfig.NO_INDENTATION);

    // act
    String json = jsonFormat.write(bean);

    // assert
    assertThat(json).isEqualTo(JSON);
  }

  /**
   * Test of unmarshalling {@link TestBean} from JSON.
   */
  @Test
  void testUnmarshallFromJson() {

    // arrange
    TestBean bean = new TestBean();
    StructuredTextFormat jsonFormat = StandardFormat.json();

    // act
    jsonFormat.read(JSON, bean);

    // assert
    assertThat(bean.Name.get()).isEqualTo(NAME);
    assertThat(bean.Age.getValue()).isEqualTo(AGE);
  }

}
