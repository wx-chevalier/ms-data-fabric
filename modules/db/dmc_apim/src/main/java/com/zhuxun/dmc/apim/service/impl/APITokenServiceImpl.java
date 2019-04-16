package com.zhuxun.dmc.apim.service.impl;

import com.zhuxun.dmc.apim.config.properties.ApplicationProperties;
import com.zhuxun.dmc.apim.config.properties.SecurityProperties;
import com.zhuxun.dmc.apim.domain.api.APITokenEntity;
import com.zhuxun.dmc.apim.service.APITokenService;
import com.zhuxun.dmc.apim.service.AbstractService;
import com.zhuxun.dmc.apim.service.AuthorizationService;
import com.zhuxun.dmc.apim.web.rest.errors.client.NotAuthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static java.lang.String.format;

@Service
public class APITokenServiceImpl extends AbstractService implements APITokenService {
  private AuthorizationService authorizationService;

  private String secretKey;

  private long tokenValidityInMilliseconds;

  private long tokenValidityInMillisecondsForRememberMe;

  @Autowired
  public APITokenServiceImpl(AuthorizationService authorizationService,
                             ApplicationProperties applicationProperties) {
    this.authorizationService = authorizationService;

    SecurityProperties.Authentication.Jwt jwt =
        applicationProperties.getSecurity().getTokenAuthentication().getJwt();
    this.secretKey = jwt.getSecret();
    this.tokenValidityInMilliseconds = jwt.getTokenValidityInSeconds() * 1000;
    this.tokenValidityInMillisecondsForRememberMe =
        jwt.getTokenValidityInSecondsForRememberMe() * 1000;
  }

  @Override
  public APITokenEntity createAPIAccessToken(String clientId, String apiId, String envId, String clientClaim, Boolean rememberMe) {
    if (!authorizationService.isAPIAuthorizedToUser(apiId, clientId)) {
      throw new NotAuthorizedException(
          format("API{id=%s} is not authorized to User{id=%s}", apiId, clientId));
    }

    return new APITokenEntity()
        .setApiId(apiId)
        .setClientId(clientId)
        .setEnvId(envId)
        .setClientClaim(clientClaim)
        .setValidity(new Date(new Date().getTime() + (rememberMe
            ? tokenValidityInMillisecondsForRememberMe
            : tokenValidityInMilliseconds)));
  }

  @Override
  public String createAPIAccessJWTToken(String clientId, String apiId, String envId, String clientClaim, Boolean rememberMe) {
    return createAPIAccessToken(clientId, apiId, envId, clientClaim, rememberMe)
        .toJWTToken(secretKey);
  }
}
