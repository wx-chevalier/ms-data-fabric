package com.zhuxun.spring.web.rest.errors.server;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ServiceUnavailableException extends ServerErrorException {
  @Getter protected HttpStatus status = HttpStatus.SERVICE_UNAVAILABLE;

  public ServiceUnavailableException() {}

  public ServiceUnavailableException(String message) {
    super(message);
  }

  public ServiceUnavailableException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServiceUnavailableException(Throwable cause) {
    super(cause);
  }

  public ServiceUnavailableException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
