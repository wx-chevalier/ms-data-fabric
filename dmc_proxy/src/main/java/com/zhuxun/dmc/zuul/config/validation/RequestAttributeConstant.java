package com.zhuxun.dmc.zuul.config.validation;

/**
 * 作用：Reques中用到的常量信息
 *
 * <p>时间：18-7-11 上午10:01
 *
 * <p>位置：com.zhuxun.dmc.zuul.config.validation
 *
 * @author Yan - tao
 */
public interface RequestAttributeConstant {

  String ErrorStatusCode = "javax.servlet.error.status_code";

  String ErrorException = "javax.servlet.error.exception";

  String ErrorMessage = "javax.servlet.error.message";

  String ProxyErrorMessage = "com.zhuxun.dmc.proxy.error.message";

  String ProxyErrorCode = "com.zhuxun.dmc.proxy.error.message.code";
}
