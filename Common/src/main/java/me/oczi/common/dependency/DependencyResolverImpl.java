package me.oczi.common.dependency;

import me.oczi.common.api.dependency.resolver.DependencyResolver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.logging.Logger;

public class DependencyResolverImpl implements DependencyResolver {
  private Logger logger;

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

  @Override
  public void setLogger(Logger logger) {
    this.logger = logger;
  }

  public void logger(String msg) {
    if (logger == null) { return; }
    logger.info(msg);
  }
}
