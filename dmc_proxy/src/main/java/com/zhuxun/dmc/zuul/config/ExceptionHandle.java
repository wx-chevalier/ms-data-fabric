package com.zhuxun.dmc.zuul.config;

import com.zhuxun.dmc.zuul.domain.exception.WebApplicationException;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
@Slf4j
public class ExceptionHandle {
  @ExceptionHandler(value = WebApplicationException.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> processWebApplicationException(WebApplicationException e) {
    return new ResponseEntity<>(new ErrorResponse(e), e.getStatus());
  }

  @Accessors(chain = true)
  @Data
  static class ErrorResponse {
    String message;

    public ErrorResponse(String message) {
      this.message = message;
    }

    public ErrorResponse(Throwable e) {
      this.message = e.getMessage();
    }
  }
}
