package com.zhuxun.dmc.apim.dto.api.apiimpl;

import com.zhuxun.dmc.apim.domain.api.apiimpl.APIImplDef;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@NoArgsConstructor
@Accessors(chain = true)
@Data
public class APIImplExecution {
  APIImplDef impl;

  Map<String, Object> param;
}
