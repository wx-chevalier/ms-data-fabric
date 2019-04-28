package com.zhuxun.dmc.user.web.rest.errors.client;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class BadRequestException extends ClientErrorException {
  @Getter
  protected HttpStatus status = HttpStatus.BAD_REQUEST;

  public BadRequestException() {
  }

  public BadRequestException(String message) {
    super(message);
  }

  public BadRequestException(String message, Throwable cause) {
    super(message, cause);
  }

  public BadRequestException(Throwable cause) {
    super(cause);
  }

  public BadRequestException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
