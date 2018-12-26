package com.zhuxun.spring.config;

import com.zhuxun.spring.web.rest.errors.WebApplicationException;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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

  @ExceptionHandler(value = Exception.class)
  @ResponseBody
  public ResponseEntity<ErrorResponse> processGeneralException(Exception e) {
    log.error("Escaped exception", e);
    return new ResponseEntity<>(new ErrorResponse(e), HttpStatus.BAD_REQUEST);
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
