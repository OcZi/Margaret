package me.oczi.common.dependency;

import me.oczi.common.api.Loggable;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.List;

/**
 * Dependency manager responsible to download and load it to the ClassLoader.
 */
public interface DependencyManager extends Loggable {

  /**
   * Add dependencies to the list of dependencies to be process.
   * @param dependency Dependency to add
   * @return DependencyManager.
   */
  DependencyManager addDependency(Dependency dependency);

  /**
   * Add dependencies to the list of dependencies to be process.
   * @param dependencies Dependencies to ddd
   * @return DependencyManager.
   */
  DependencyManager addDependency(Dependency... dependencies);

  /**
   * Add dependencies to the list of dependencies to be process.
   * @param dependencies Dependencies to add
   * @return DependencyManager.
   */
  DependencyManager addDependency(Collection<Dependency> dependencies);

  /**
   * Process all the dependency's list.
   * @return List of all dependencies processed.
   * @throws IOException Throw if a error occurs in dependency downloading.
   * @throws InvocationTargetException Throw if a error occurs dependency loading.
   * @throws IllegalAccessException Throw if a error occurs dependency loading.
   */
  List<Dependency> process() throws IOException, InvocationTargetException, IllegalAccessException;

  /**
   * Set the path used to save all the files into it.
   * @param path
   */
  void setPath(File path);

  /**
   * Get all the dependencies to be process.
   * @return Dependency list.
   */
  List<Dependency> getDependencyList();

  /**
   * Set DependencyManager into offline mode.
   * Will not download any file, just get the dependency file
   * in lib folder and check their hashes to load.
   * @param offlineMode Set offline mode.
   */
  void setOfflineMode(boolean offlineMode);
}
