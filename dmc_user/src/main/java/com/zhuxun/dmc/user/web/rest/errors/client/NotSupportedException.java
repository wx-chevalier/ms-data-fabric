package com.zhuxun.dmc.user.web.rest.errors.client;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class NotSupportedException extends ClientErrorException {
  @Getter
  protected HttpStatus status = HttpStatus.UNSUPPORTED_MEDIA_TYPE;

  public NotSupportedException() {
  }

  public NotSupportedException(String message) {
    super(message);
  }

  public NotSupportedException(String message, Throwable cause) {
    super(message, cause);
  }

  public NotSupportedException(Throwable cause) {
    super(cause);
  }

  public NotSupportedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
