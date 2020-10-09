/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.examples;

/**
 * {@link TestBean} that is {@link #isDynamic() dynamic}.
 */
public class DynamicTestBean extends TestBean {

  @Override
  public boolean isDynamic() {

    return true;
  }

}
