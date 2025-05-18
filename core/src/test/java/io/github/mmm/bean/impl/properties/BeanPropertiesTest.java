package io.github.mmm.bean.impl.properties;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import io.github.mmm.property.WritableProperty;
import io.github.mmm.property.string.StringProperty;

/**
 * Abstract base test for implementations of {@link BeanProperties}.
 */
public abstract class BeanPropertiesTest {

  /**
   * @return a new and empty instance of the {@link BeanProperties} implementation to test.
   */
  protected abstract BeanProperties create();

  /** Tests an empty instance of {@link BeanProperties}. */
  @Test
  public void testEmpty() {

    // arrange
    // act
    BeanProperties properties = create();
    Collection<? extends WritableProperty<?>> collection = properties.get();
    // assert
    assertThat(properties.get("foo")).isNull();
    assertThat(collection.size()).isZero();
    assertThat(collection.isEmpty()).isTrue();
    assertThat(collection.iterator()).isExhausted();
    assertThat(collection).hasToString("[]");
  }

  /** Test of {@link BeanProperties#add(WritableProperty)} and {@link BeanProperties#get(String)}. */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Test
  public void testAdd() {

    // arrange
    BeanProperties properties = create();
    StringProperty p1 = new StringProperty("First");
    StringProperty p2 = new StringProperty("Grand");
    StringProperty p3 = new StringProperty("Hammond");
    StringProperty p4 = new StringProperty("Insert");
    StringProperty p5 = new StringProperty("Last");
    List<WritableProperty<?>> list = new ArrayList<>(Arrays.asList(p3, p5, p4, p2, p1));
    Collections.shuffle(list);
    // act
    for (WritableProperty<?> property : list) {
      properties.add(property);
    }
    // assert
    for (WritableProperty<?> property : list) {
      assertThat(properties.get(property.getName())).isSameAs(property);
    }
    assertThat((Collection) properties.get()).containsExactly(p1, p2, p3, p4, p5);
  }

  /**
   * Test of {@link BeanProperties#addIfAbsent(String, java.util.function.Function)} and
   * {@link BeanProperties#get(String)}.
   */
  @SuppressWarnings({ "rawtypes", "unchecked" })
  @Test
  public void testAddIfAbsent() {

    // arrange
    BeanProperties properties = create();
    StringProperty p1 = new StringProperty("First");
    StringProperty p2 = new StringProperty("Grand");
    StringProperty p3 = new StringProperty("Hammond");
    StringProperty p4 = new StringProperty("Insert");
    StringProperty p5 = new StringProperty("Last");
    List<WritableProperty<?>> list = new ArrayList<>(Arrays.asList(p3, p5, p4, p2, p1));
    Collections.shuffle(list);
    // act
    for (WritableProperty<?> property : list) {
      WritableProperty<?> property2 = properties.addIfAbsent(property.getName(), name -> new StringProperty(name));
      assertThat(property2).isNotSameAs(property);
    }
    // assert
    for (WritableProperty<?> property : list) {
      assertThat(properties.get(property.getName())).isNotSameAs(property).isEqualTo(property);
    }
    assertThat((Collection) properties.get()).containsExactly(p1, p2, p3, p4, p5);
    // act + assert on update
    for (WritableProperty<?> property : properties.get()) {
      WritableProperty<?> property2 = properties.addIfAbsent(property.getName(), name -> new StringProperty(name));
      assertThat(property2).isSameAs(property);
    }
  }

}
