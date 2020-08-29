package me.oczi.common.exceptions;

import java.sql.SQLException;

public class SQLNotSafeException extends SQLException {

  public SQLNotSafeException(String errMessage, Throwable t) {
    super(errMessage, t);
  }

  public SQLNotSafeException(String errMessage) {
    super(errMessage);
  }
}
