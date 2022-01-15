package io.github.mmm.bean.mapping;

import io.github.mmm.bean.impl.DatatypeMapperImpl;

/**
 * Interface for a simple component capable of {@link #getName(Class) getting the name} for a datatype given as
 * {@link Class} and {@link #getType(String) vice-versa}. The implementation is designed in a secure way as described in
 * {@link #getType(String)}.
 *
 * @since 1.0.0
 * @see #get()
 */
public interface DatatypeMapper {

  /**
   * @param type the {@link Class} reflecting the given datatype (e.g. {@link String}, {@link Boolean}, {@link Long},
   *        etc.).
   * @return the name of the given datatype. By default this is the {@link Class#getName() qualified name} but it may
   *         also be the {@link Class#getSimpleName() simple name} as long as it is unique.
   */
  String getName(Class<?> type);

  /**
   * @param name the {@link #getName(Class) name} of the datatype.
   * @return the {@link Class} reflecting the datatype with the given {@code name}. Will be {@code null} if the datatype
   *         is not known. For security reasons by design no reflective lookup (classloading) is triggered. This allows
   *         to deserialize or unmarshall polymorphic or generic data without allowing external users (or attackers) to
   *         trigger classloading and potential code execution.
   */
  Class<?> getType(String name);

  /**
   * @return the {@link DatatypeMapperImpl}.
   */
  public static DatatypeMapper get() {

    return DatatypeMapperImpl.INSTANCE;
  }

}
