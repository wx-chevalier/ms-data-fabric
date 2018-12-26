package com.zhuxun.dmc.apim.dto;

import lombok.Data;

import java.util.Set;

@Data
public class AuthorizeResourceTO {

  // 被授权人id
  private String authorizedUserId;

  // 授权资源id
  private Set<String> authorizedResId;
}
