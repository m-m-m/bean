/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.test;

import java.time.LocalDate;
import java.time.Period;

import io.github.mmm.property.PropertyMetadata;
import io.github.mmm.property.number.integers.IntegerProperty;
import io.github.mmm.property.temporal.localdate.LocalDateProperty;

/**
 * Extends {@link PersonBean} for testing.
 */
@SuppressWarnings("javadoc")
public interface ContactBean extends PersonBean {

  LocalDateProperty Birthday();

  LocalDate getBirthday();

  void setBirthday(LocalDate birthday);

  @Override
  default IntegerProperty Age() {

    return new IntegerProperty("Age", PropertyMetadata.ofExpression(() -> {
      LocalDate birthday = getBirthday();
      if (birthday == null) {
        return null;
      }
      return Period.between(birthday, LocalDate.now()).getYears();
    }));
  }

}
