package com.zhuxun.spring.web.rest.errors.server;

import com.zhuxun.spring.web.rest.errors.WebApplicationException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

public class ServerErrorException extends WebApplicationException {
  @Getter protected HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

  public ServerErrorException() {}

  public ServerErrorException(String message) {
    super(message);
  }

  public ServerErrorException(String message, Throwable cause) {
    super(message, cause);
  }

  public ServerErrorException(Throwable cause) {
    super(cause);
  }

  public ServerErrorException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
