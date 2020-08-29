package me.oczi.bukkit.other.exceptions;

public class IdCollisionException extends RuntimeException {

  public IdCollisionException(String errMessage) {
    super(errMessage);
  }

  public IdCollisionException(String errMessage, Throwable t) {
    super(errMessage, t);
  }
}
