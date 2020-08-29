package me.oczi.common.api.dependency;

import me.oczi.common.dependency.Dependency;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

/**
 * Dependency manager responsible to download and load it to the ClassLoader.
 */
public interface DependencyManager {

  DependencyManager addDependency(Dependency dependency);

  DependencyManager addDependency(Dependency... dependencies);

  DependencyManager addDependency(Collection<Dependency> dependencies);

  List<Dependency> process() throws IOException, InvocationTargetException, IllegalAccessException;

  void setPath(File path);

  List<Dependency> getDependencyList();
}
