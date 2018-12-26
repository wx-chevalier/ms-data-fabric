package com.zhuxun.spring.security.jwt;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class AccessAPIToken extends AbstractAuthenticationToken {

  @Getter String principal;

  @Getter String envId;

  public AccessAPIToken(
      String userId, String envId, Collection<? extends GrantedAuthority> authorities) {
    super(authorities);
    this.principal = userId;
    this.envId = envId;
    super.setAuthenticated(authorities != null);
  }

  public AccessAPIToken(String userId, String envId) {
    this(userId, envId, null);
  }

  @Override
  public Object getCredentials() {
    return null;
  }

  public String getUserId() {
    return principal;
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
