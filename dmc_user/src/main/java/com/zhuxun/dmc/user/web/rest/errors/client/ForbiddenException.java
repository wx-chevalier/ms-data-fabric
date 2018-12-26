package com.zhuxun.dmc.user.web.rest.errors.client;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ForbiddenException extends ClientErrorException {
  @Getter
  protected HttpStatus status = HttpStatus.FORBIDDEN;

  public ForbiddenException() {
  }

  public ForbiddenException(String message) {
    super(message);
  }

  public ForbiddenException(String message, Throwable cause) {
    super(message, cause);
  }

  public ForbiddenException(Throwable cause) {
    super(cause);
  }

  public ForbiddenException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
