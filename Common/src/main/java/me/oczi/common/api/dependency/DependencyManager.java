package me.oczi.common.api.dependency;

import me.oczi.common.dependency.Dependency;
import me.oczi.common.dependency.maven.MavenDependency;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

public interface DependencyManager {

  DependencyManager addDependency(Dependency dependency);

  DependencyManager addDependency(Dependency... dependencies);

  DependencyManager addDependency(Collection<Dependency> dependencies);

  List<MavenDependency> process() throws IOException, InvocationTargetException, IllegalAccessException;

  void setPath(File path);

  List<MavenDependency> getDependencyList();
}
