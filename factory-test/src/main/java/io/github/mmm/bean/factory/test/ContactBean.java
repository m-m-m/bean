/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.test;

import java.time.LocalDate;
import java.time.Period;

import io.github.mmm.base.metainfo.MetaInfos;
import io.github.mmm.bean.BeanFactory;
import io.github.mmm.property.PropertyMetadata;
import io.github.mmm.property.number.integers.IntegerProperty;
import io.github.mmm.property.time.localdate.LocalDateProperty;

/**
 * Extends {@link PersonBean} for testing.
 */
@SuppressWarnings("javadoc")
@MetaInfos("table=CONTACT")
public interface ContactBean extends PersonBean {

  @MetaInfos({ "column=DATE_OF_BIRTH", "precision=7" })
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

  static ContactBean of() {

    return BeanFactory.get().create(ContactBean.class);
  }

}
