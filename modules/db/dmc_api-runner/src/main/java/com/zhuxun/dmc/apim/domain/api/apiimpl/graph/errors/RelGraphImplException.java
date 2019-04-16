package com.zhuxun.dmc.apim.domain.api.apiimpl.graph.errors;

public class RelGraphImplException extends RuntimeException {
  public RelGraphImplException() {
  }

  public RelGraphImplException(String message) {
    super(message);
  }

  public RelGraphImplException(String message, Throwable cause) {
    super(message, cause);
  }

  public RelGraphImplException(Throwable cause) {
    super(cause);
  }

  public RelGraphImplException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
