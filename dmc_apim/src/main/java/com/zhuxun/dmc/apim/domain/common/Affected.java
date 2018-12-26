package com.zhuxun.dmc.apim.domain.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
public class Affected {

  private int count;
}
