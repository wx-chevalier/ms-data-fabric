package com.zhuxun.dc.apirunner.exception;

import com.zhuxun.dc.apirunner.exception.SQLAPIException;

public class SchemaValidationError extends SQLAPIException {
  public SchemaValidationError() {
  }

  public SchemaValidationError(String message) {
    super(message);
  }

  public SchemaValidationError(String message, Throwable cause) {
    super(message, cause);
  }

  public SchemaValidationError(Throwable cause) {
    super(cause);
  }

  public SchemaValidationError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
