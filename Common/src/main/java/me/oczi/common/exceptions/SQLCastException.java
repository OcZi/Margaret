package me.oczi.common.exceptions;

public class SQLCastException extends SQLRuntimeException {

  public SQLCastException(Class<?> clazz) {
    super("Impossible cast to " + clazz.getTypeName());
  }

  public SQLCastException(String prefixes,
                          Class<?> clazz) {
    super("Impossible cast to " + prefixes + clazz.getTypeName());
  }

  public SQLCastException(Class<?> clazz,
                          Throwable throwable) {
    super("Impossible cast to " + clazz.getTypeName(),
        throwable);
  }

  public SQLCastException(String prefixes,
                          Class<?> clazz,
                          Throwable throwable) {
    super("Impossible cast to " + prefixes + clazz.getTypeName(),
        throwable);
  }

  public SQLCastException(String errMessage) {
    super(errMessage);
  }
}
