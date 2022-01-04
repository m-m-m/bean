
/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */

/**
 * Module for testing only!
 */
// https://github.com/m-m-m/bean/issues/4
// Should also work without open keyword but does not due to bugs or design flaws in Java
open module io.github.mmm.bean.test {

  requires transitive io.github.mmm.bean.factory;

}
