package com.zhuxun.dmc.apim.service;

import com.zhuxun.dmc.apim.domain.api.APITokenEntity;

public interface APITokenService {
  APITokenEntity createAPIAccessToken(String clientId, String apiId, String envId, String clientClaim, Boolean rememberMe);

  String createAPIAccessJWTToken(String clientId, String apiId, String envId, String clientClaim, Boolean rememberMe);
}
