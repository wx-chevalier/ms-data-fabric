package com.zhuxun.dc.apirunner.utils;

import com.zhuxun.dc.apirunner.entity.TokenEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.*;

import static com.zhuxun.dc.apirunner.config.SecretConstant.JWT_SECRET;
import static com.zhuxun.dc.apirunner.config.SecretConstant.SIGNATURE_ALGORITHM;

public class JwtUtils {

  public static TokenEntity parseTokenToAccount(String token, String secret, String realmName) {
    Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    String roleString = (String) body.get("roles");
    String permissionString = (String) body.get("permissions");

    return new TokenEntity()
        .userId((String) body.get("userId"))
        .username((String) body.get("username"))
        .roles(new HashSet<>(Arrays.asList(roleString.split(","))))
        .permissions(new HashSet<>(Arrays.asList(permissionString.split(","))))
        .expireAt(new Long(String.valueOf(body.get("expireAt"))));
  }

  /**
   * @param userName
   * @param roles
   * @param secret
   * @param expireTime seconds
   * @return
   */
  public static String createAuthToken(
      String userId,
      String userName,
      Set<String> roles,
      Set<String> permissions,
      String secret,
      Long expireTime) {
    Claims claims = Jwts.claims();
    claims.put("username", userName);
    claims.put("roles", String.join(",", roles.toArray(new String[0])));
    claims.put("permissions", String.join(",", permissions.toArray(new String[0])));
    claims.put("expireAt", System.currentTimeMillis() + expireTime * 1000);
    claims.put("userId", userId);

    return String.format(
        "Bearer: %s",
        Jwts.builder().setClaims(claims).signWith(SIGNATURE_ALGORITHM, secret).compact());
  }

  /**
   * 生成测试token
   *
   * @param userId 当前用户的ID
   * @param apiId  当前APIID
   * @param secret
   * @return
   */
  public static String creteTestToken(String userId, String apiId, String secret) {
    Claims claims = Jwts.claims();
    claims.put("userId", userId);
    claims.put("apiId", apiId);
    String testToken =
        String.format(
            "Bearer: %s",
            Jwts.builder().setClaims(claims).signWith(SIGNATURE_ALGORITHM, JWT_SECRET).compact());
    return testToken;
  }

  /**
   * 解析测试Token
   *
   * @param secret
   * @param token
   * @return
   */
  public static Map<String, Object> parseTestToken(String secret, String token) {
    Claims body = Jwts.parser().setSigningKey(secret).parseClaimsJws(token.substring(7)).getBody();
    Map<String, Object> map = new HashMap<String, Object>();
    map.put("userId", body.get("userId"));
    map.put("apiId", body.get("apiId"));
    return map;
  }
}
