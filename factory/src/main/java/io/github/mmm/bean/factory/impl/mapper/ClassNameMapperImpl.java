package io.github.mmm.bean.factory.impl.mapper;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.github.mmm.base.collection.AbstractIterator;
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

    LOG.debug("Adding class {} for name {} and type {}.", container.javaClass, container.qualifiedName,
        container.classType);
    ClassNameTypeContainer duplicateContainer = this.class2containerMap.put(container.javaClass, container);
    if (duplicateContainer != null) {
      throw new DuplicateObjectException(container.javaClass.getName(), container.qualifiedName,
          duplicateContainer.qualifiedName);
    }
    Class<?> duplicateClass = this.name2typeMap.put(container.name, container.javaClass);
    if (duplicateClass != null) {
      throw new DuplicateObjectException(container.name, container.javaClass.getName(), duplicateClass.getName());
    }
    if (container.name != container.simpleName) {
      Class<?> mappedClass = this.name2typeMap.putIfAbsent(container.simpleName, container.javaClass);
      if (mappedClass != null) {
        duplicateContainer = this.class2containerMap.get(mappedClass);
        Class<?> unmappedClass = container.javaClass;
        if (container.priority > duplicateContainer.priority) {
          this.name2typeMap.put(container.simpleName, container.javaClass);
          unmappedClass = mappedClass;
          mappedClass = container.javaClass;
        }
        LOG.warn("Duplicate simple name {} mapped to class {} and not to {}", container.simpleName, mappedClass,
            unmappedClass);
      }
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

  private static class ClassIterator extends AbstractIterator<Class<?>> {

    private final Iterator<ClassNameTypeContainer> it;

    private final ClassType classType;

    private ClassIterator(Iterator<ClassNameTypeContainer> it, ClassType classType) {

      super();
      this.it = it;
      this.classType = classType;
      findFirst();
    }

    @Override
    protected Class<?> findNext() {

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
