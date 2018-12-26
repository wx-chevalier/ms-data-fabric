package com.zhuxun.dmc.apim.dto.api.apiimpl;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@NoArgsConstructor
@Accessors(chain = true)
@Data
public class APIImplExecution {
  APIImplTO impl;

  Map<String, Object> param;
}
