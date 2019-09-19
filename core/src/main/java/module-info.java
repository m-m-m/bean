/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
module net.sf.mmm.bean {

  requires transitive net.sf.mmm.property;

  provides net.sf.mmm.property.factory.PropertyFactory //
      with net.sf.mmm.bean.property.PropertyFactoryBean; //

  exports net.sf.mmm.bean;

  exports net.sf.mmm.bean.property;

}
