package com.zhuxun.dmc.user.web.rest.errors.client;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NotAuthorizedException extends ClientErrorException {
  @Getter
  protected HttpStatus status = HttpStatus.UNAUTHORIZED;

  public NotAuthorizedException() {
  }

  public NotAuthorizedException(String message) {
    super(message);
  }

  public NotAuthorizedException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotAuthorizedException(Throwable cause) {
    super(cause);
  }

  public NotAuthorizedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
