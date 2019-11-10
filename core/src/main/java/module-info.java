/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides advanced Java beans based on {@code mmm-property}.
 */
@SuppressWarnings("all") //
module io.github.mmm.bean {

  requires transitive io.github.mmm.property;

  provides io.github.mmm.property.factory.PropertyFactory //
      with io.github.mmm.bean.property.PropertyFactoryBean; //

  exports io.github.mmm.bean;

  exports io.github.mmm.bean.property;

}
