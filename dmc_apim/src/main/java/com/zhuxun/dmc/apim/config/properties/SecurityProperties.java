package com.zhuxun.dmc.apim.config.properties;

import com.zhuxun.dmc.apim.config.ApplicationDefaults;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;

@Accessors(chain = true)
@Data
public class SecurityProperties {

  private final ClientAuthorization clientAuthorization = new ClientAuthorization();

  private final Authentication authentication = new Authentication();

  /**
   * API Access Token 创建配置
   */
  private final Authentication tokenAuthentication = new Authentication();

  private final RememberMe rememberMe = new RememberMe();

  @Accessors(chain = true)
  @Data
  public static class ClientAuthorization {

    private String accessTokenUri = ApplicationDefaults.Security.ClientAuthorization.accessTokenUri;

    private String tokenServiceId = ApplicationDefaults.Security.ClientAuthorization.tokenServiceId;

    private String clientId = ApplicationDefaults.Security.ClientAuthorization.clientId;

    private String clientSecret = ApplicationDefaults.Security.ClientAuthorization.clientSecret;
  }

  @Accessors(chain = true)
  @Data
  public static class Authentication {

    private final Authentication.Jwt jwt = new Authentication.Jwt();

    @Accessors(chain = true)
    @Data
    public static class Jwt {

      private String secret = ApplicationDefaults.Security.Authentication.Jwt.secret;

      private long tokenValidityInSeconds = ApplicationDefaults.Security.Authentication.Jwt.tokenValidityInSeconds;

      private long tokenValidityInSecondsForRememberMe = ApplicationDefaults.Security.Authentication.Jwt.tokenValidityInSecondsForRememberMe;
    }
  }

  @Accessors(chain = true)
  @Data
  public static class RememberMe {

    @NotNull
    private String key = ApplicationDefaults.Security.RememberMe.key;
  }
}
