package com.zhuxun.dmc.apim.domain.auth;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class BearerToken {
  String token;
}
