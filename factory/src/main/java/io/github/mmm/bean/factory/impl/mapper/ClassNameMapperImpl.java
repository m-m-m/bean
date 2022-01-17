package io.github.mmm.bean.factory.impl.mapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.exception.DuplicateObjectException;
import io.github.mmm.base.exception.ObjectNotFoundException;
import io.github.mmm.bean.mapping.ClassNameMapper;
import io.github.mmm.property.factory.PropertyFactoryManager;

/**
 * Implementation of {@link ClassNameMapper}.
 *
 * @since 1.0.0
 */
public class ClassNameMapperImpl implements ClassNameMapper {

  private static final Logger LOG = LoggerFactory.getLogger(ClassNameMapperImpl.class);

  private Map<String, Class<?>> name2typeMap;

  private Map<Class<?>, ClassNameTypeContainer> class2containerMap;

  /**
   * The constructor.
   */
  public ClassNameMapperImpl() {

    super();
    this.name2typeMap = new HashMap<>();
    this.class2containerMap = new HashMap<>();
    init();
  }

  /**
   * Initialized this class.
   */
  void init() {

    for (Class<?> type : PropertyFactoryManager.get().getValueTypes()) {
      addDatatype(type);
    }
    for (Class<?> type : BeanTypeSet.INSTANCE.getBeanTypes()) {
      addBean(type);
    }
  }

  void addDatatype(Class<?> datatype) {

    add(new ClassNameTypeContainer(datatype, ClassType.DATATYPE));
  }

  void addBean(Class<?> beanType) {

    add(new ClassNameTypeContainer(beanType, ClassType.BEAN));
  }

  void add(ClassNameTypeContainer container) {

    LOG.debug("Adding class {} for name {} and type {}.", container.javaClass, container.name, container.classType);
    ClassNameTypeContainer duplicateContainer = this.class2containerMap.put(container.javaClass, container);
    if (duplicateContainer != null) {
      throw new DuplicateObjectException(container.javaClass.getName(), container.name, duplicateContainer.name);
    }
    Class<?> duplicateClass = this.name2typeMap.put(container.name, container.javaClass);
    if (duplicateClass != null) {
      throw new DuplicateObjectException(container.name, container.javaClass.getName(), duplicateClass.getName());
    }
  }

  @Override
  public String getName(Class<?> javaClass) {

    ClassNameTypeContainer container = this.class2containerMap.get(javaClass);
    if (container == null) {
      throw new ObjectNotFoundException("ClassNameTypeContainer", javaClass.getName());
    }
    return container.name;
  }

  @Override
  public Class<?> getClass(String name) {

    Class<?> javaClass = this.name2typeMap.get(name);
    if (javaClass == null) {
      throw new ObjectNotFoundException("Class", name);
    }
    return javaClass;
  }

  @Override
  public boolean contains(Class<?> javaClass) {

    return this.class2containerMap.containsKey(javaClass);
  }

  @Override
  public boolean contains(String name) {

    return this.name2typeMap.containsKey(name);
  }

  @Override
  public Iterator<Class<?>> getClasses(ClassType classType) {

    return new ClassIterator(this.class2containerMap.values().iterator(), classType);
  }

  private static class ClassIterator implements Iterator<Class<?>> {

    private final Iterator<ClassNameTypeContainer> it;

    private final ClassType classType;

    private Class<?> next;

    private ClassIterator(Iterator<ClassNameTypeContainer> it, ClassType classType) {

      super();
      this.it = it;
      this.classType = classType;
      this.next = findNext();
    }

    @Override
    public boolean hasNext() {

      return this.next != null;
    }

    @Override
    public Class<?> next() {

      if (this.next == null) {
        throw new NoSuchElementException();
      }
      Class<?> result = this.next;
      this.next = findNext();
      return result;
    }

    private Class<?> findNext() {

      while (this.it.hasNext()) {
        ClassNameTypeContainer container = this.it.next();
        if ((this.classType == null) || (this.classType == container.classType)) {
          return container.javaClass;
        }
      }
      return null;
    }
  }

}
