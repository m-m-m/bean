package io.github.mmm.bean.impl.mapping;

import java.util.Map;

import io.github.mmm.base.filter.CharFilter;
import io.github.mmm.bean.mapping.PropertyIdMapping;
import io.github.mmm.property.ReadableProperty;

/// Implementation of [PropertyIdMapping] based on [Map].
public abstract class AbstractPropertyIdMapping implements PropertyIdMapping {

  @Override
  public int id(ReadableProperty<?> property) {

    String name = property.getName();
    int id = id(name);
    if (id == -1) {
      int len = name.length();
      if ((len > 0) && (len < 9) && (CharFilter.LATIN_DIGIT.accept(name.charAt(0)))) {
        try {
          id = Integer.parseInt(name);
        } catch (NumberFormatException e) {
          // ignore
        }
      }
    }
    return id;
  }

  /// @param name the [name][ReadableProperty#getName()] of the [property][ReadableProperty].
  /// @return the ID of the property or `null`.
  protected abstract int id(String name);

}
