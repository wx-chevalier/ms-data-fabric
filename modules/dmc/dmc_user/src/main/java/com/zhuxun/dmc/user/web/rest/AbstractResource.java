package com.zhuxun.dmc.user.web.rest;

import com.zhuxun.dmc.user.domain.sec.User;
import com.zhuxun.dmc.user.security.SecurityUtils;
import com.zhuxun.dmc.user.security.jwt.JWTAuthenticationToken;
import com.zhuxun.dmc.user.security.jwt.TokenProvider;
import org.springframework.security.core.Authentication;

import java.util.Optional;

public abstract class AbstractResource {
  protected TokenProvider tokenProvider;

  public AbstractResource(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  protected Optional<User> currentUser() {
    Authentication authentication = SecurityUtils.authentication();
    if (authentication instanceof JWTAuthenticationToken) {
      JWTAuthenticationToken jwthAuth = (JWTAuthenticationToken) authentication;
      return Optional.of(new User()
          .setId(jwthAuth.getUserId())
          .setName(jwthAuth.getName()));
    } else {
      return Optional.empty();
    }
  }
}
