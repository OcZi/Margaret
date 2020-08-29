package me.oczi.common.exceptions;

public class NotInstantiatedClassException extends RuntimeException {

  public NotInstantiatedClassException() {
    super("This class cannot be instantiated!");
  }

  public NotInstantiatedClassException(Throwable t) {
    super("This class cannot be instantiated!", t);
  }
}
