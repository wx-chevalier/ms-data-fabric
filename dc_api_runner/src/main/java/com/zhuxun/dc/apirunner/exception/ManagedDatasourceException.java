package com.zhuxun.dc.apirunner.exception;

public class ManagedDatasourceException extends Exception {
  public ManagedDatasourceException() {
  }

  public ManagedDatasourceException(String message) {
    super(message);
  }

  public ManagedDatasourceException(String message, Throwable cause) {
    super(message, cause);
  }

  public ManagedDatasourceException(Throwable cause) {
    super(cause);
  }

  public ManagedDatasourceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
