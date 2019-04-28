package com.zhuxun.dmc.zuul.aop;

import com.zhuxun.dmc.zuul.domain.exception.WebApplicationException;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
  @ExceptionHandler(WebApplicationException.class)
  protected ResponseEntity<Object> handleRestExceptions(WebApplicationException ex) {
    return new ResponseEntity<>(
        new ApiError(ex.getStatus(), ex.getMessage()),
        ex.getStatus());
  }

  @Accessors(chain = true)
  @Data
  public static class ApiError {
    private HttpStatus status;
    private String message;

    public ApiError(HttpStatus status, String message) {
      this.status = status;
      this.message = message;
    }
  }
}
