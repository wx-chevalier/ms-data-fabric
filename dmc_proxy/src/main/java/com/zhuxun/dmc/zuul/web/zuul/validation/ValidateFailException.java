package com.zhuxun.dmc.zuul.web.zuul.validation;

/**
 * 作用： OpenAPI-Specification 校验失败异常信息
 *
 * <p>时间：2018/7/4 10:22</p>
 *
 * <p>位置：com.zhuxun.dmc.zuul.web.zuul.validation</p>
 *
 * @author Yan - tao
 */
public class ValidateFailException extends RuntimeException {

  private String message;

  public ValidateFailException(String message) {
    super(message);
    this.message = message;
  }
}
