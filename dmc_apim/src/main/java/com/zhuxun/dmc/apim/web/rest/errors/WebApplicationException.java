package com.zhuxun.dmc.apim.web.rest.errors;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class WebApplicationException extends RuntimeException {
  @Getter
  protected HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

  public WebApplicationException() {
  }

  public WebApplicationException(String message) {
    super(message);
  }

  public WebApplicationException(String message, Throwable cause) {
    super(message, cause);
  }

  public WebApplicationException(Throwable cause) {
    super(cause);
  }

  public WebApplicationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
