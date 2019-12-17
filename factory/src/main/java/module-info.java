/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
/**
 * Provides {@link io.github.mmm.bean.BeanFactory} to create beans also from interfaces as dynamic proxies. If you want
 * to have no boilerplate code at all, like to have multi-inheritance and do not fear magic, you can even define your
 * beans as interfaces. To instantiate such bean, you need to use {@link io.github.mmm.bean.BeanFactory#create(Class)}.
 */
module io.github.mmm.bean.factory {

  requires transitive io.github.mmm.bean;

  exports io.github.mmm.bean.factory;

}
