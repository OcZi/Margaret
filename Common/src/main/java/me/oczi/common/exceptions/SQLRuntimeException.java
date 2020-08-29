package me.oczi.common.exceptions;

public class SQLRuntimeException extends RuntimeException {

  public SQLRuntimeException() {}

  public SQLRuntimeException(String errMessage, Throwable throwable) {
    super(errMessage, throwable);
  }

  public SQLRuntimeException(String errMessage) {
    super(errMessage);
  }

  public SQLRuntimeException(Throwable throwable) {
    super(throwable);
  }
}
