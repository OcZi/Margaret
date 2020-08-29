package me.oczi.common.exceptions;

public class RequiredMetadataException extends RuntimeException {

  public RequiredMetadataException(String errMessage, Throwable throwable) {
    super(errMessage, throwable);
  }

  public RequiredMetadataException(String errMessage) {
    super(errMessage);
  }
}
