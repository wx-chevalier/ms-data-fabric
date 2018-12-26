package com.zhuxun.dmc.user.domain.auth;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class BearerToken {
  String token;
}
