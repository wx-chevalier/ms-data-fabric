package com.zhuxun.dmc.apim.service;

import java.util.Set;

public interface AuthorizationService {
  void authorizeAPI(String authorizerUserId, String apiId, Set<String> userIds);

  void deauthAPI(String authorizerUserId, String apiId, Set<String> userIds);

  Boolean isAPIAuthorizedToUser(String apiId, String userId);
}
