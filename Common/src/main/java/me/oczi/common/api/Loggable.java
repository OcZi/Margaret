package me.oczi.common.api;

import java.util.logging.Logger;

public interface Loggable {

  void info(String string);

  void info(String... string);

  void warning(String string);

  void warning(String... string);

  void setLogger(Logger logger);
}
