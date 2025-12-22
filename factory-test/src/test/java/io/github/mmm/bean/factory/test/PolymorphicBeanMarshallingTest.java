package io.github.mmm.bean.factory.test;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import io.github.mmm.marshall.MarshallingConfig;
import io.github.mmm.marshall.StandardFormat;
import io.github.mmm.marshall.StructuredTextFormat;

/**
 * Test of marshalling and unmarshalling a {@link io.github.mmm.bean.Bean#isPolymorphic() polymorphic}
 * {@link io.github.mmm.bean.Bean} via {@link Comestible}.
 */
public class PolymorphicBeanMarshallingTest {

  private static final String JSON_WATER = "{\"@type\":\"Drink\",\"Name\":\"water\"}";

  private static final String JSON_RICE = "{\"@type\":\"Food\",\"Name\":\"rice\"}";

  @Test
  void testReadJsonDrink() {

    // arrange
    Comestible comestible = Comestible.of();
    StructuredTextFormat jsonFormat = StandardFormat.json();

    // act
    Comestible result = jsonFormat.read(JSON_WATER, comestible);

    // assert
    assertThat(result).isNotNull();
    assertThat(result.Name().get()).isEqualTo("water");
    assertThat(result.getJavaClass()).isEqualTo(Drink.class);
  }

  @Test
  void testReadJsonFood() {

    // arrange
    Comestible comestible = Comestible.of();
    StructuredTextFormat jsonFormat = StandardFormat.json();

    // act
    Comestible result = jsonFormat.read(JSON_RICE, comestible);

    // assert
    assertThat(result).isNotNull();
    assertThat(result.Name().get()).isEqualTo("rice");
    assertThat(result.getJavaClass()).isEqualTo(Food.class);
  }

  @Test
  void testWriteJsonDrink() {

    // arrange
    Drink water = Drink.of();
    water.Name().set("water");
    StructuredTextFormat jsonFormat = StandardFormat.json(MarshallingConfig.NO_INDENTATION);

    // act
    String json = jsonFormat.write(water);

    // assert
    assertThat(json).isEqualTo(JSON_WATER);
  }

  @Test
  void testWriteJsonFood() {

    // arrange
    Food rice = Food.of();
    rice.Name().set("rice");
    StructuredTextFormat jsonFormat = StandardFormat.json(MarshallingConfig.NO_INDENTATION);

    // act
    String json = jsonFormat.write(rice);

    // assert
    assertThat(json).isEqualTo(JSON_RICE);
  }

}
