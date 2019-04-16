package com.zhuxun.dmc.sqlapi.errors;

public class SQLAPIException extends Exception {
  public SQLAPIException() {
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

  public SQLAPIException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
