package com.zhuxun.dc.apirunner.shiro.realm;

import lombok.Getter;
import org.apache.shiro.authc.HostAuthenticationToken;

/**
 * Created by lotuc on 14/04/2017.
 */
public class JwtAuthenticationToken implements HostAuthenticationToken {
  @Getter
  private String host;

  @Getter
  private String authToken;

  public JwtAuthenticationToken(String host, String authToken) {
    this.host = host;
    this.authToken = authToken;
  }

  @Override
  public Object getPrincipal() {
    return this.authToken;
  }

  @Override
  public Object getCredentials() {
    return null;
  }
}
