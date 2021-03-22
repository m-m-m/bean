
/*
 * Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
import io.github.mmm.bean.BeanFactory;
import io.github.mmm.bean.impl.BeanCreator;
import io.github.mmm.bean.property.PropertyFactoryBean;
import io.github.mmm.property.factory.PropertyFactory;

/**
 * Provides advanced Java beans based on {@code mmm-property}.<br>
 * <h2>Bean</h2><br>
 * Writing regular Java Beans is painful. You have to write a lot of boilerplate code and implement getters, setters,
 * equals, hashCode, and toString.<br>
 * The {@link io.github.mmm.bean.WritableBean} API provided here saves you from all this pain and makes your life a lot
 * easier.
 * <h3>Class implementation</h3><br>
 * To implement a bean you only need to extend from {@link io.github.mmm.bean.Bean}:
 *
 * <pre>
 * public class TestBean extends Bean {
 *
 *   public final StringProperty Name;
 *
 *   public final IntegerProperty Age;
 *
 *   public TestBean() {
 *
 *     this(null, true);
 *   }
 *
 *   public TestBean(AbstractBean writable, boolean dynamic) {
 *
 *     super(writable, dynamic);
 *     this.Name = add(new StringProperty("Name"));
 *     this.Age = add(new IntegerProperty("Age"));
 *   }
 * }
 * </pre>
 *
 * Now you can do things like this:
 *
 * <pre>
 * TestBean bean = new TestBean();
 * bean.Name.set("John Doe");
 * bean.Age.set(42);
 * // Read-Only views
 * TestBean readonly = WritableBean.getReadOnly(bean);
 * assertThat(readonly.Age.get()).isEqualTo(42);
 * bean.Age.set(43);
 * assertThat(readonly.Age.get()).isEqualTo(43);
 * try {
 *   readonly.Age.set(44);
 *   fail("Exception expected");
 * } catch (IllegalStateException e) {
 * }
 * // Change listener...
 * bean.Age.addListener((e) {@literal ->} {
 *   System.out.println(e.getOldValue() + "{@literal -->}" + e.getValue());
 * });
 * bean.Age.set(44); // prints: 43 {@literal -->} 44
 * // Copy and compare
 * TestBean bean2 = new TestBean();
 * for (WritableProperty{@literal <?>} property : bean.getProperties()) {
 *   bean2.set(property.getName(), property.getValue());
 * }
 * assertThat(bean.isEqualTo(bean2)).isTrue();
 * </pre>
 *
 * <h3>Interface only</h3><br>
 * If you want to have no boilerplate code at all, like to have multi-inheritance and do not fear magic, you can even
 * define your beans as interfaces only. Then you can instantiate them using {@code BeanFactory} from the module
 * {@code io.github.mmm.bean.factory} of {@code mmm-bean-factory}.
 */
@SuppressWarnings("all") //
module io.github.mmm.bean {

  requires transitive io.github.mmm.property;

  requires transitive io.github.mmm.property.builder;

  uses BeanFactory;

  provides BeanFactory with BeanCreator;

  provides PropertyFactory with PropertyFactoryBean;

  exports io.github.mmm.bean;

  exports io.github.mmm.bean.mapping;

  exports io.github.mmm.bean.property;

  exports io.github.mmm.bean.impl to io.github.mmm.bean.factory;

}
