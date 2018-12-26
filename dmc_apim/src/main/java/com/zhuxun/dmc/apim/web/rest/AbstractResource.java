package com.zhuxun.dmc.apim.web.rest;

import com.zhuxun.dmc.apim.domain.sec.User;
import com.zhuxun.dmc.apim.security.SecurityUtils;
import com.zhuxun.dmc.apim.security.jwt.JWTAuthenticationToken;
import com.zhuxun.dmc.apim.security.jwt.TokenProvider;
import com.zhuxun.dmc.apim.web.rest.errors.client.NotAuthorizedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.servletapi.SecurityContextHolderAwareRequestWrapper;

import java.util.Optional;

public abstract class AbstractResource {
  protected TokenProvider tokenProvider;

  public AbstractResource(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  protected User currentUserOrThrow() {
    return currentUser().orElseThrow(NotAuthorizedException::new);
  }

  protected Optional<User> currentUser() {
    Authentication authentication = SecurityUtils.authentication();
    if (authentication instanceof JWTAuthenticationToken) {
      JWTAuthenticationToken jwtAuth = (JWTAuthenticationToken) authentication;
      return Optional.of(new User()
          .setId(jwtAuth.getUserId())
          .setName(jwtAuth.getName()));
    } else {
      return Optional.empty();
    }
  }
}
