package com.zhuxun.dmc.apim.web.rest.errors;

public class RedirectionException extends WebApplicationException {
  public RedirectionException() {
    super();
  }

  public RedirectionException(String message) {
    super(message);
  }

  public RedirectionException(String message, Throwable cause) {
    super(message, cause);
  }

  public RedirectionException(Throwable cause) {
    super(cause);
  }

  protected RedirectionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
