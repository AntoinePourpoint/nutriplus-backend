package io.ajl.nutriplus.exception;

public class InvalidBarcodeException extends RuntimeException {
  public InvalidBarcodeException(String message) {
    super(message);
  }
}
