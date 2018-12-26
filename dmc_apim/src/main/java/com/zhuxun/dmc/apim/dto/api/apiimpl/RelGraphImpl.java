package com.zhuxun.dmc.apim.dto.api.apiimpl;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class RelGraphImpl extends APIImplTO {
  String graph;

  String def;

  @Override
  APIImplType getType() {
    return APIImplType.REL_GRAPH;
  }
}
