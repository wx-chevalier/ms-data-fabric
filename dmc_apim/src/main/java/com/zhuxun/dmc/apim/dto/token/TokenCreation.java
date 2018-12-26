package com.zhuxun.dmc.apim.dto.token;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class TokenCreation {
  String envId;
  String clientClaim;
  Boolean rememberMe = Boolean.FALSE;
}
