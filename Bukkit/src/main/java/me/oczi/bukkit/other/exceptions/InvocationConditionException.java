package me.oczi.bukkit.other.exceptions;

public class InvocationConditionException extends RuntimeException {

  InvocationConditionException(Throwable throwable) {
    super(throwable);
  }

  @Override
  public synchronized Throwable fillInStackTrace() {
    return this;
  }
}
