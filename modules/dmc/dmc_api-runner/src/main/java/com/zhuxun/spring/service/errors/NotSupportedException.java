package com.zhuxun.spring.service.errors;

public class NotSupportedException extends RuntimeException {
  public NotSupportedException() {}

  public NotSupportedException(String message) {
    super(message);
  }

  public NotSupportedException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotSupportedException(Throwable cause) {
    super(cause);
  }

  public NotSupportedException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
