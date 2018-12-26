package com.zhuxun.dc.apirunner.exception;

public class SQLAPIException extends Exception {
  public SQLAPIException() {
    super();
  }

  public SQLAPIException(String message) {
    super(message);
  }

  public SQLAPIException(String message, Throwable cause) {
    super(message, cause);
  }

  public SQLAPIException(Throwable cause) {
    super(cause);
  }

  protected SQLAPIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
