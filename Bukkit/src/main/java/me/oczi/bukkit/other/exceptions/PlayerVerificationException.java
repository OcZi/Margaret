package me.oczi.bukkit.other.exceptions;

public class PlayerVerificationException extends RuntimeException{

  public PlayerVerificationException(String errMessage) {
    super(errMessage);
  }

  public PlayerVerificationException(Throwable throwable) {
    super(throwable);
  }
}
