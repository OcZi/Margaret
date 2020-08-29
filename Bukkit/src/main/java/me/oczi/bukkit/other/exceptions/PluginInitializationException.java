package me.oczi.bukkit.other.exceptions;

public class PluginInitializationException extends Exception {

  public PluginInitializationException(String errMessage,
                                       Throwable throwable) {
    super(errMessage, throwable);
  }

  public PluginInitializationException(Throwable throwable) {
    super(throwable);
  }
}
