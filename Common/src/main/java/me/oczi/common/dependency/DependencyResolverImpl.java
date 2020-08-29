package me.oczi.common.dependency;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Class used to load any dependency in a {@link URLClassLoader}
 */
public class DependencyResolverImpl implements DependencyResolver {
  private URLClassLoader urlClassLoader;
  private Method ADD_URL_METHOD;

  public DependencyResolverImpl(URLClassLoader classLoader) {
    urlClassLoader = classLoader;
    try {
      ADD_URL_METHOD = URLClassLoader.class
          .getDeclaredMethod("addURL", URL.class);
      ADD_URL_METHOD.setAccessible(true);
    } catch (NoSuchMethodException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void addUrl(URL url)
      throws InvocationTargetException, IllegalAccessException {
    ADD_URL_METHOD.invoke(urlClassLoader, url);
  }
}
