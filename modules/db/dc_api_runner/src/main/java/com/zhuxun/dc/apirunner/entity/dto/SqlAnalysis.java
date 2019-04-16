package com.zhuxun.dc.apirunner.entity.dto;

import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class SqlAnalysis {
  String dialect;
  String sql;
  String datasourceName;
}
