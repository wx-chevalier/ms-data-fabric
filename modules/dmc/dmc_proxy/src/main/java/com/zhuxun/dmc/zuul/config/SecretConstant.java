package com.zhuxun.dmc.zuul.config;

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * 安全验证的相关参数
 *
 * @author tao
 */
public interface SecretConstant {

  String JWT_SECRET = "secret";

  SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

  /**
   *  API 代理请求的头部验证KEY
   */
  String TURELORE_TOKEN = "truelore-key";
}
