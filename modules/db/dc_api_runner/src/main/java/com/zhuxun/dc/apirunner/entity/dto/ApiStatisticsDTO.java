package com.zhuxun.dc.apirunner.entity.dto;

import com.zhuxun.dc.apirunner.dao.entity.DcApiStatisticsExample;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ApiStatisticsDTO {

  String startTime;

  String endTime;

  String groupByFormat;

  String apiId;

  public DcApiStatisticsExample of() {
    return new DcApiStatisticsExample();
  }
}
