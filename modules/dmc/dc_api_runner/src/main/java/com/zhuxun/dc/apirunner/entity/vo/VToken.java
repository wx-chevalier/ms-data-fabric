package com.zhuxun.dc.apirunner.entity.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dc.apirunner.config.SecretConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Optional;

/** Created by lotuc on 28/04/2017. */
@Data
@NoArgsConstructor
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VToken {

  private String token;

  private String userId;

  private String apiId;

  private String envId;

  public VToken(String token) {
    this.token = token;
  }

  public static VToken of(String token) {
    Claims body = Jwts.parser().setSigningKey(SecretConstant.JWT_SECRET).parseClaimsJws(token.substring(7)).getBody();
    return Optional.ofNullable(token)
        .map(
            tokenMap -> {
              VToken vToken = new VToken();
              return vToken
                  .setApiId((String) body.get("apiId"))
                  .setUserId((String) body.get("userId"))
                  .setEnvId((String) body.get("envId"));
            })
        .orElse(null);
  }
}
