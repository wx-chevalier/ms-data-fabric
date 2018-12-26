package com.zhuxun.dmc.runner.service.errors;

import com.zhuxun.spring.service.errors.ServiceException;

public class InCompleteDefinitionError extends ServiceException {
  public InCompleteDefinitionError() {
  }

  public InCompleteDefinitionError(String message) {
    super(message);
  }

  public InCompleteDefinitionError(String message, Throwable cause) {
    super(message, cause);
  }

  public InCompleteDefinitionError(Throwable cause) {
    super(cause);
  }

  public InCompleteDefinitionError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
