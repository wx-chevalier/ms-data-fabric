package com.zhuxun.dmc.zuul.domain.token;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dmc.zuul.config.SecretConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Optional;

/** Created by lotuc on 28/04/2017. */
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VToken {

  private String token;

  private String userId;

  private String apiId;

  private String envId;

  public VToken(){}

  public VToken(String token) {
    this.token = token;
  }

  public static VToken of(String token) {
    Claims body = Jwts.parser().setSigningKey(SecretConstant.JWT_SECRET).parseClaimsJws(token).getBody();
    return Optional.ofNullable(token)
        .map(
            tokenMap -> {
              VToken vToken = new VToken();
              return vToken
                  .setApiId((String) body.get("API_ID"))
                  .setUserId((String) body.get("sub"))
                  .setEnvId((String) body.get("ENV_KEY"));
            })
        .orElse(null);
  }
}
