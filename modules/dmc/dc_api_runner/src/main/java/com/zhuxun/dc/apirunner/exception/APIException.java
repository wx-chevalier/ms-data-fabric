package com.zhuxun.dc.apirunner.exception;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * Created by lotuc on 27/06/2017.
 */
public class APIException extends RuntimeException {

  @JsonProperty("statusCode")
  @Getter
  protected HttpStatus statusCode;

  @JsonProperty("message")
  @Getter
  protected String message;

  public APIException() {
    this("服务器内部错误");
  }

  public APIException(HttpStatus statusCode) {
    this.statusCode = statusCode;
  }

  public APIException(String message) {
    this.message = message;
    this.statusCode = HttpStatus.BAD_REQUEST;
  }

  public APIException(HttpStatus statusCode, String message) {
    this.statusCode = statusCode;
    this.message = message;
  }
}
