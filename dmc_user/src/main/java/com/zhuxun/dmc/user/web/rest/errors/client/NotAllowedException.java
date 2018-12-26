package com.zhuxun.dmc.user.web.rest.errors.client;


import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NotAllowedException extends ClientErrorException {
  @Getter
  protected HttpStatus status = HttpStatus.METHOD_NOT_ALLOWED;

  public NotAllowedException() {
  }

  public NotAllowedException(String message) {
    super(message);
  }

  public NotAllowedException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotAllowedException(Throwable cause) {
    super(cause);
  }

  public NotAllowedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
