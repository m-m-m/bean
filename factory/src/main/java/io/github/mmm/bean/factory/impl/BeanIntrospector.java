/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.bean.Bean;
import io.github.mmm.bean.factory.impl.operation.BeanOperation;
import io.github.mmm.bean.factory.impl.operation.BeanOperationProperty;

/**
 * Collector for {@link BeanOperation}s.
 *
 * @since 1.0.0
 */
public class BeanIntrospector {

  /** Logger instance. */
  private static final Logger LOG = LoggerFactory.getLogger(BeanIntrospector.class);

  private final Class<?> beanType;

  private final Map<String, BeanOperation> propertyMap;

  private final Set<Class<?>> typesVisited;

  /**
   * The constructor.
   *
   * @param beanType primary bean type.
   */
  public BeanIntrospector(Class<?> beanType) {

    super();
    this.beanType = beanType;
    this.propertyMap = new HashMap<>();
    this.typesVisited = new HashSet<>();
  }

  /**
   * @param type the {@link Class} reflecting the {@link Bean} or one of its parent types to visit.
   * @return {@code true} if the given {@link Class} should be visited (introspected), {@code false} if it has already
   *         been visited.
   */
  public boolean visitType(Class<?> type) {

    boolean added = this.typesVisited.add(type);
    if (added) {
      LOG.trace("{}: Introspecting type {}", this.beanType, type);
    } else {
      LOG.trace("{}: Already visited type {}", this.beanType, type);
    }
    return added;
  }

  /**
   * @param operation the {@link BeanOperation} to register.
   */
  public void add(BeanOperation operation) {

    String propertyName = operation.getPropertyName();
    if (propertyName == null) {
      return;
    }
    if (operation instanceof BeanOperationProperty) {
      BeanOperation existing = this.propertyMap.get(propertyName);
      if (!(existing instanceof BeanOperationProperty)) {
        this.propertyMap.put(propertyName, operation);
      } // else the property method is overridden
    } else {
      this.propertyMap.putIfAbsent(propertyName, operation);
    }
  }

  /**
   * @param name the name of the property.
   * @return the {@link BeanOperation} that was {@link #add(BeanOperation) added} for the given property name or
   *         {@code null} if none was found.
   */
  public BeanOperation getPropertyOperation(String name) {

    return this.propertyMap.get(name);
  }

  /**
   * @return the {@link Collection} of {@link BeanOperation}s for property creation.
   */
  public Collection<BeanOperation> getPropertyOperations() {

    return new ArrayList<>(this.propertyMap.values());
  }

}
