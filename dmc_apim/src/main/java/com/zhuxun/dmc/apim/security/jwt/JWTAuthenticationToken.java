package com.zhuxun.dmc.apim.security.jwt;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class JWTAuthenticationToken extends AbstractAuthenticationToken {
  @Getter
  String principal;

  @Getter
  String username;

  @Override
  public Object getCredentials() {
    return null;
  }

  public String getUserId() {
    return principal;
  }

  public JWTAuthenticationToken(String userId, String username,
                                Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = userId;
    this.username = username;
    super.setAuthenticated(authorities != null);
  }

  public JWTAuthenticationToken(String userId, String username) {
    this(userId, username, null);
  }

  @Override
  public void setAuthenticated(boolean authenticated) {
    if (authenticated) {
      throw new IllegalArgumentException(
          "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
    }

    super.setAuthenticated(false);
  }
}
