package com.Exceptions;

import org.openqa.selenium.NoSuchElementException;

public class FrameworkException extends RuntimeException {

  private Integer errorCode;

  public FrameworkException() {
  }

  public FrameworkException(String message) {
    super(message);
  }

  public FrameworkException(String message, Throwable cause) {
    super(message, cause);
  }

  public FrameworkException(Throwable cause) {
    super(cause);
  }

  public FrameworkException(String message, Throwable cause, ErrorCodes errorCode) {
    super(message, cause);
    this.errorCode = errorCode.getCode();
  }

  public FrameworkException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

enum ErrorCodes {
  VALIDATION_PARSE_ERROR(123);
  private int code;

  ErrorCodes(int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }

  public void wrapWithCustomException() {
    try {
      // some code…
    } catch (NoSuchElementException e) {
      throw new FrameworkException(
          "A business-friendly message describing the caught NoSuchElementException: ", e);
    }
  }
}
