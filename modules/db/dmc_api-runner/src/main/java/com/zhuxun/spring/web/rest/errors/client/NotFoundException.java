package com.zhuxun.spring.web.rest.errors.client;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NotFoundException extends ClientErrorException {
  @Getter protected HttpStatus status = HttpStatus.NOT_FOUND;

  public NotFoundException() {}

  public NotFoundException(String message) {
    super(message);
  }

  public NotFoundException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotFoundException(Throwable cause) {
    super(cause);
  }

  public NotFoundException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
