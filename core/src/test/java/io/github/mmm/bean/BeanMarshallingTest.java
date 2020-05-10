/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import io.github.mmm.bean.examples.TestBean;
import io.github.mmm.marshall.StructuredFormat;
import io.github.mmm.marshall.StructuredFormatFactory;

/**
 * Test of marshalling and unmarshalling {@link io.github.mmm.bean.Bean} via {@link TestBean}.
 */
public class BeanMarshallingTest extends Assertions {

  private static final int AGE = 42;

  private static final String NAME = "John Doe";

  private static final String JSON = "{\"Age\":42,\"Name\":\"John Doe\"}";

  /**
   * Test of marshalling {@link TestBean} to JSON.
   */
  @Test
  public void testMarshallToJson() {

    // given
    TestBean bean = new TestBean();
    bean.Name.set(NAME);
    bean.Age.setValue(AGE);
    StructuredFormat jsonFormat = StructuredFormatFactory.get().create(StructuredFormat.ID_JSON);

    // when
    String json = jsonFormat.write(bean);

    // then
    assertThat(json).isEqualTo(JSON);
  }

  /**
   * Test of unmarshalling {@link TestBean} from JSON.
   */
  @Test
  public void testUnmarshallFromJson() {

    // given
    String json = JSON;
    TestBean bean = new TestBean();
    StructuredFormat jsonFormat = StructuredFormatFactory.get().create(StructuredFormat.ID_JSON);

    // when
    jsonFormat.read(JSON, bean);

    // then
    assertThat(bean.Name.get()).isEqualTo(NAME);
    assertThat(bean.Age.getValue()).isEqualTo(AGE);
  }

}
