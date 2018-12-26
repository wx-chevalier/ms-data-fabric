package com.zhuxun.spring.web.rest.errors.client;

import com.zhuxun.spring.web.rest.errors.WebApplicationException;

public class ClientErrorException extends WebApplicationException {
  public ClientErrorException() {}

  public ClientErrorException(String message) {
    super(message);
  }

  public ClientErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  public ClientErrorException(Throwable cause) {
    super(cause);
  }

  public ClientErrorException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
