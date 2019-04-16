package com.zhuxun.dc.apirunner.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

/**
 * Created by lotuc on 20/04/2017.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(fluent = true, chain = true)
public class TokenEntity {
  public static final TokenEntity EMPTY_TOKEN = new TokenEntity();

  String userId;
  String username;
  Set<String> roles;
  Set<String> permissions;
  /**
   * 单位：ms
   */
  Long expireAt;

  public boolean isExpired() {
    return this.expireAt < System.currentTimeMillis();
  }
}
