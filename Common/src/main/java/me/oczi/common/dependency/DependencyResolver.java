package me.oczi.common.dependency;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;

public interface DependencyResolver {

  /**
   * Load a URL into the ClassLoader.
   * @param url URL to load.
   * @throws InvocationTargetException Throw if a error occurs in the process.
   * @throws IllegalAccessException Throw if a error occurs when trying to call method by reflection.
   */
  void addUrl(URL url) throws InvocationTargetException, IllegalAccessException;
}
