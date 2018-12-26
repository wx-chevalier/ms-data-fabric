package com.zhuxun.dmc.zuul.config.validation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * 作用：校验错误代码
 *
 * <p>时间：18-7-11 上午10:15
 *
 * <p>位置：com.zhuxun.dmc.zuul.config.validation
 *
 * @author Yan - tao
 */
@AllArgsConstructor
public enum ValidationErrorType {

  // 权限验证异常
  ERROR_SERVICE_TOKEN_AUTH("TOKEN授权异常", 1000),
  ERROR_SERVICE_TOKEN_PARSE("TOKEN解析异常", 1010),
  ERROR_SERVICE_TOKEN_NOT_EXIST("没有找到TOKEN", 1011),
  ERROR_SERVICE_TOKEN_EXPIRED("TOKEN已经过期,尝试使用新的ToKEN", 1012),
  ERROR_SERVER_TOKEN_ABANDON("Token无效或已被禁用,请重新生成Token后重试",1013),

  // 序列化异常
  ERROR_SERVICE_DESERIALIZE("反序列化校验模型出错", 3000),
  ERROR_SERVICE_JSON_SYNTAX("请求体JSON反序列化异常", 3301),
  // 校验异常错误代码
  ERROR_SERVICE_VALIDAT_QUERY("查询参数校验失败", 4101),
  ERROR_SERVICE_VALIDATE_HEADER("请求头校验失败", 4201),
  ERROR_SERVICE_VALIDATE_REQUEST_BODY("请求体校验失败", 4301),

  // 未知异常
  ERROR_SYSTEM_UNKNOWN_REASON("未知异常", 5901),

  //代理目标服务器异常
  ERROR_PROXY_TARGET("代理目标地址请求异常,无法访问",6011);

  @Setter
  @Getter
  @Accessors(chain = true, fluent = true)
  private String message;

  /**
   * 错误代码规范
   *
   * <p>授权错误:10XX
   *
   * <p>token解析错误:1010
   *
   * <p>token不存在错误:1011
   *
   * <p>模型反序列化异常:30XX
   *
   * <p>请求体反序列化异常:33XX
   *
   * <p>查询参数校验错误:41XX
   *
   * <p>请求头校验错误:42XX
   *
   * <p>请求体校验错误页:43XX
   *
   * <p>代理服务其异常:61XX
   *
   * <p>未知异常:59XX
   */
  @Setter
  @Getter
  @Accessors(chain = true, fluent = true)
  private int code;
}
