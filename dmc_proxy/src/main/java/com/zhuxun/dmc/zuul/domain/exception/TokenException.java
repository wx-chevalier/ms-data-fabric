package com.zhuxun.dmc.zuul.domain.exception;

import org.springframework.http.HttpStatus;

/**
 * Token解析或者无效异常
 *
 * @author tao
 */
public class TokenException extends WebApplicationException {

  private String message;

  public TokenException(String message) {
    super(message);
    this.message = message;
    this.status = HttpStatus.UNAUTHORIZED;
  }
}
