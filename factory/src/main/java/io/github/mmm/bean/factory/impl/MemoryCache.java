/* Copyright (c) The m-m-m Team, Licensed under the Apache License, Version 2.0
 * http://www.apache.org/licenses/LICENSE-2.0 */
package io.github.mmm.bean.factory.impl;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/// A simple cache implementation that is thread-safe and uses [WeakReference]s to avoid memory leaks.
///
/// @param <K> type of [key][Map#containsKey(Object)].
/// @param <V> type of [value][Map#containsValue(Object)].
///
/// @since 1.0.0
public class MemoryCache<K, V> {

  private final ReentrantLock lock;

  private final Map<K, WeakReference<V>> cache;

  /// The constructor.
  public MemoryCache() {

    super();
    this.lock = new ReentrantLock();
    this.cache = new HashMap<>(32);
  }

  /// @param key the [key][Map#containsKey(Object)].
  /// @param factory the [Supplier] factory.
  /// @return the cached value or the value created from the given [Supplier] factory after putting into this
  ///         cache.
  /// @see Map#computeIfAbsent(Object, java.util.function.Function)
  public V get(K key, Supplier<V> factory) {

    this.lock.lock();
    try {
      // WeakReference<V> weakReference = this.cache.computeIfAbsent(key, x -> new WeakReference<>(factory.get()));
      WeakReference<V> weakReference = this.cache.get(key);
      V value = null;
      if (weakReference != null) {
        value = weakReference.get();
      }
      if (value == null) {
        value = factory.get();
        weakReference = new WeakReference<>(value);
        this.cache.put(key, weakReference);
      }
      return value;
    } finally {
      this.lock.unlock();
    }
  }

}
