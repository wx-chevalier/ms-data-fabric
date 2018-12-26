package com.zhuxun.dc.apirunner.config;

import io.jsonwebtoken.SignatureAlgorithm;

public class SecretConstant {

  public static final String JWT_SECRET = "secret";


  public static SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;
}
