/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * Provides {@link io.github.mmm.bean.BeanFactory} to create beans also from interfaces as dynamic proxies. If you want
 * to have no boilerplate code at all, like to have multi-inheritance and do not fear magic, you can even define your
 * beans as interfaces. To instantiate such bean, you need to use {@link io.github.mmm.bean.BeanFactory#create(Class)}.
 *
 * @provides io.github.mmm.bean.BeanCreator
 * @provides io.github.mmm.bean.mapping.ClassNameMapper
 */
module io.github.mmm.bean.factory {

  requires transitive io.github.mmm.bean;

  requires io.github.classgraph;

  provides io.github.mmm.bean.BeanCreator with io.github.mmm.bean.factory.impl.BeanInterfaceCreator;

  provides io.github.mmm.bean.mapping.ClassNameMapper with io.github.mmm.bean.factory.impl.mapper.ClassNameMapperImpl;

  exports io.github.mmm.bean.factory.scanner;

  exports io.github.mmm.bean.factory.impl.bean to io.github.mmm.bean;

}
