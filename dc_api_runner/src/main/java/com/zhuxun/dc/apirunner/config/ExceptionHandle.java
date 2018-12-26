package com.zhuxun.dc.apirunner.config;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhuxun.dc.apirunner.exception.ManagedDatasourceException;
import com.zhuxun.dc.apirunner.exception.SchemaValidationError;
import com.zhuxun.dc.apirunner.exception.ServiceException;
import com.zhuxun.dc.apirunner.exception.TokenException;
import com.zhuxun.dc.apirunner.sqlapi.SQLValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;


@ControllerAdvice
@Slf4j
public class ExceptionHandle {


  @ExceptionHandler(value = IllegalArgumentException.class)
  @ResponseBody
  public ResponseEntity<ErrResponse> processIllegalArgumentException(IllegalArgumentException e) {
    log.warn("出现 IllegalArgumentException 异常信息", e);
    return new ResponseEntity<>(new ErrResponse(e), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = IOException.class)
  @ResponseBody
  public ResponseEntity<ErrResponse> processIOException(IOException e) {
    log.warn("出现 IOException 异常信息", e);
    return new ResponseEntity<>(new ErrResponse("解析文件出现异常,请检查配置文件信息"), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = TokenException.class)
  @ResponseBody
  public ResponseEntity<ErrResponse> processTokenException(TokenException e) {
    log.warn("出现 TokenException 异常信息", e);
    return new ResponseEntity<>(new ErrResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = SQLValidationError.class)
  @ResponseBody
  public ResponseEntity<ErrResponse> processSQLValidationError(SQLValidationError e) {
    log.warn("SQL 校验错误", e);
    return new ResponseEntity<>(new ErrResponse(e.getMessage()), HttpStatus.UNPROCESSABLE_ENTITY);
  }

  @ExceptionHandler(value = SchemaValidationError.class)
  @ResponseBody
  public ResponseEntity<ErrResponse> processSchemaValidationError(SchemaValidationError e) {
    log.warn("Schema 校验未通过", e);
    return new ResponseEntity<>(new ErrResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(value = ManagedDatasourceException.class)
  @ResponseBody
  public ResponseEntity<ErrResponse> processSchemaValidationError(ManagedDatasourceException e) {
    log.warn("数据源错误", e);
    return new ResponseEntity<>(new ErrResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(value = ServiceException.class)
  @ResponseBody
  public ResponseEntity<ErrResponse> processSchemaValidationError(ServiceException e) {
    log.warn("Internal service error", e);
    return new ResponseEntity<>(new ErrResponse(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  static class ErrResponse {
    @JsonProperty("message")
    private String message;

    public ErrResponse(String message) {
      this.message = message;
    }

    public ErrResponse(RuntimeException e) {
      this.message = e.getMessage();
    }
  }


}
