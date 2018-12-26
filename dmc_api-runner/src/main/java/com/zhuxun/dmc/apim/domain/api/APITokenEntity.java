package com.zhuxun.dmc.apim.domain.api;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Data
public class APITokenEntity {
  static final String CLIENT_CLAIM_KEY = "CLIENT_CLAIM";
  static final String API_ID_KEY = "API_ID";
  static final String ENV_ID_KEY = "ENV_KEY";

  /** 申请接口访问 TOKEN 的企业用户 */
  private String clientId;

  /** 企业用户申请 TOKEN 时附带的信息 */
  private String clientClaim;

  private String apiId;

  private String envId;

  /** 过期时间 */
  private Date validity;

  public String toJWTToken(String secretKey) {
    return Jwts.builder()
        .setSubject(clientId)
        .claim(CLIENT_CLAIM_KEY, clientClaim)
        .claim(API_ID_KEY, apiId)
        .claim(ENV_ID_KEY, envId)
        .signWith(SignatureAlgorithm.HS512, secretKey)
        .setExpiration(validity)
        .compact();
  }
}
