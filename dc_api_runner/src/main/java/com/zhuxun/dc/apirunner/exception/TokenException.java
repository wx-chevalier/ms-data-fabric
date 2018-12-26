package com.zhuxun.dc.apirunner.exception;

/**
 * 解析Token可能出现的异常信息
 *
 * @author tao
 */

public class TokenException extends RuntimeException {

  private String message;


  public TokenException(String message) {
    super(message);
    this.message = message;
  }


}
