package com.zhuxun.dmc.apim.dto.api;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class APITokenTO {
  /**
   * JWT token
   */
  String token;

  String apiURL;
}
