package com.zhuxun.dmc.user.web.rest.errors.server;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class InternalServerErrorException extends ServerErrorException {
  @Getter
  protected HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

  public InternalServerErrorException() {
  }

  public InternalServerErrorException(String message) {
    super(message);
  }

  public InternalServerErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  public InternalServerErrorException(Throwable cause) {
    super(cause);
  }

  public InternalServerErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
