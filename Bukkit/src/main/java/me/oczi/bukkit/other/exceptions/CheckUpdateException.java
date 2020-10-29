package me.oczi.bukkit.other.exceptions;

public class CheckUpdateException extends Exception {

  public CheckUpdateException(String errMessage, Throwable t) {
    super(errMessage, t);
  }

  public CheckUpdateException(String errMessage) {
    super(errMessage);
  }

  public CheckUpdateException(Throwable t) {
    super(t);
  }
}