package me.oczi.bukkit.utils.lib;

import me.oczi.common.dependency.DependencyManager;
import me.oczi.common.dependency.Dependency;
import me.oczi.common.dependency.DependencyManagerImpl;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * Implementation of {@link DependencyManager}.
 */
public class LibraryLoader implements DependencyManager {
  private final DependencyManager delegate;

  public LibraryLoader(File path, ClassLoader classLoader) {
    this(path, classLoader, null);
  }

  public LibraryLoader(File path, ClassLoader classLoader, @Nullable Logger logger) {
    this.delegate = new DependencyManagerImpl(path, classLoader, logger);
  }

  @Override
  public DependencyManager addDependency(Dependency dependency) {
    return delegate.addDependency(dependency);
  }

  @Override
  public DependencyManager addDependency(Dependency... dependencies) {
    return delegate.addDependency();
  }

  @Override
  public DependencyManager addDependency(Collection<Dependency> dependencies) {
    return delegate.addDependency(dependencies);
  }

  @Override
  public List<Dependency> process()
      throws IllegalAccessException,
      IOException,
      InvocationTargetException {
    return delegate.process();
  }

  @Override
  public void setPath(File path) {
    delegate.setPath(path);
  }

  @Override
  public List<Dependency> getDependencyList() {
    return delegate.getDependencyList();
  }

  @Override
  public void setLogger(Logger logger) {
    this.delegate.setLogger(logger);
  }
}
