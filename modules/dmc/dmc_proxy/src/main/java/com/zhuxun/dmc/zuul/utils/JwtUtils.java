package com.zhuxun.dmc.zuul.utils;

import com.zhuxun.dmc.zuul.domain.token.TokenEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import java.util.*;

import static com.zhuxun.dmc.zuul.config.SecretConstant.*;

/**
 * Token生成等先关工具
 *
 * @author tao
 */
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
    map.put("envId", body.get("envId"));
    return map;
  }
}
