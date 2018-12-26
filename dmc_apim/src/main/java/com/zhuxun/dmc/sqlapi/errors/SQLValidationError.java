package com.zhuxun.dmc.sqlapi.errors;

public class SQLValidationError extends SQLAPIException {
  public SQLValidationError() {
  }

  public SQLValidationError(String message) {
    super(message);
  }

  public SQLValidationError(String message, Throwable cause) {
    super(message, cause);
  }

  public SQLValidationError(Throwable cause) {
    super(cause);
  }

  public SQLValidationError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
