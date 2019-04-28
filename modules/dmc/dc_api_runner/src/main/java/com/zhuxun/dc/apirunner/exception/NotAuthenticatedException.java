package com.zhuxun.dc.apirunner.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by lotuc on 27/06/2017.
 */
@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthenticatedException extends APIException {
  public NotAuthenticatedException(String message) {
    super(message);
    this.statusCode = HttpStatus.UNAUTHORIZED;
  }
}
