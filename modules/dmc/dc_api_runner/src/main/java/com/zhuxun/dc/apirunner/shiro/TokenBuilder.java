package com.zhuxun.dc.apirunner.shiro;

import com.zhuxun.dc.apirunner.utils.JwtUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.zhuxun.dc.apirunner.config.SecretConstant.*;


/**
 * Created by lotuc on 21/04/2017.
 */
public class TokenBuilder {
  String userId;

  String username;

  List<Map> roles;

  List<Map> permissions;

  String secret;

  Long expireTime;

  public TokenBuilder() {
    this.roles = new ArrayList<>();
    this.permissions = new ArrayList<>();
  }

  public TokenBuilder(String userId, String username, String secret, Long expireTime) {
    this();
    this.userId = userId;
    this.username = username;
    this.secret = secret;
    this.expireTime = expireTime;
  }

  public TokenBuilder(String secret, Long expireTime) {
    this();
    this.secret = secret;
    this.expireTime = expireTime;
  }

  public TokenBuilder withUserId(String userId) {
    this.userId = userId;
    return this;
  }

  public TokenBuilder withUsername(String username) {
    this.username = username;
    return this;
  }

  public TokenBuilder withRole(Map role) {
    this.roles.add(role);
    return this;
  }

  public TokenBuilder withRoles(List<Map> roles) {
    this.roles.addAll(roles);
    return this;
  }

  public TokenBuilder withPermission(Map permission) {
    this.permissions.add(permission);
    return this;
  }

  public TokenBuilder withPermissions(List<Map> permissions) {
    this.permissions.addAll(permissions);
    return this;
  }

  public TokenBuilder withSecret(String secret) {
    this.secret = secret;
    return this;
  }

  public TokenBuilder withExpireTime(Long expireTime) {
    this.expireTime = expireTime;
    return this;
  }

  public String build() {
    Set<String> permissionsSet = permissions
            .stream()
            .map(map -> (String)map.get("permission"))
            .collect(Collectors.toSet());
    Set<String> roleSet = roles
            .stream()
            .map(map -> (String)map.get("role"))
            .collect(Collectors.toSet());
    return JwtUtils.createAuthToken(userId, username, roleSet, permissionsSet, JWT_SECRET, this.expireTime);
  }
}
