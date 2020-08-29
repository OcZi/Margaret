package me.oczi.common.api.dependency.resolver;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.logging.Logger;

public interface DependencyResolver {

  void addUrl(URL url) throws InvocationTargetException, IllegalAccessException;

  void setLogger(Logger logger);
}
