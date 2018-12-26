package com.zhuxun.dmc.user.web.rest;

import com.google.common.collect.ImmutableList;
import com.zhuxun.dmc.user.config.UserConstants;
import com.zhuxun.dmc.user.security.jwt.JWTAuthenticationToken;
import com.zhuxun.dmc.user.security.jwt.TokenProvider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.zhuxun.dmc.user.security.jwt.JWTFilter.BEARER;

public abstract class AbstractIntgretionTest {
  protected abstract TokenProvider getTokenProvider();

  protected String adminToken;
  protected String managerToken;
  protected String qy1Token;
  protected String qy2Token;
  protected String qy3Token;

  protected String adminId = "69a9edb6-76f7-486f-8d4f-ce12de973363";
  protected String managerId = "6b81710b-5677-4743-b141-c6510c35593e";
  protected String qy1Id = "6defc1a2-3724-4494-84d2-2ff347a212fd";
  protected String qy2Id = "98545636-db12-4f47-8463-75c847999dea";
  protected String qy3Id = "b8bf3580-a173-43dc-b163-f86829327ae1";

  protected void initTestTokens() {
    this.adminToken = getToken(adminId, "admin", ImmutableList.of("ROLE_" + UserConstants.ROLE_ADMIN));
    this.managerToken = getToken(managerId, "manager", ImmutableList.of("ROLE_" + UserConstants.ROLE_MANAGER));
    this.qy1Token = getToken(qy1Id, "qy1", ImmutableList.of("ROLE_" + UserConstants.ROLE_CLIENT));
    this.qy2Token = getToken(qy2Id, "qy2", ImmutableList.of("ROLE_" + UserConstants.ROLE_CLIENT));
    this.qy3Token = getToken(qy3Id, "qy3", ImmutableList.of("ROLE_" + UserConstants.ROLE_CLIENT));
  }

  protected MockHttpServletRequestBuilder withToken(
      MockHttpServletRequestBuilder builder, String token) {
    if (token == null) {
      return builder;
    } else {
      return builder.header("Authorization", token);
    }
  }

  protected String getToken(String userId, String username, Collection<String> authorities) {
    return BEARER + " " + getTokenProvider().createToken(
        new JWTAuthenticationToken(
            userId, username,
            authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())),
        false);
  }
}
