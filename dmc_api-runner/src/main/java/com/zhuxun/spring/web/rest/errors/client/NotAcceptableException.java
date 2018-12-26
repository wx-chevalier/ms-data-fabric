package com.zhuxun.spring.web.rest.errors.client;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NotAcceptableException extends ClientErrorException {
  @Getter protected HttpStatus status = HttpStatus.NOT_ACCEPTABLE;

  public NotAcceptableException() {}

  public NotAcceptableException(String message) {
    super(message);
  }

  public NotAcceptableException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotAcceptableException(Throwable cause) {
    super(cause);
  }

  public NotAcceptableException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
