package com.zhuxun.dc.apirunner.entity.vo.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * API 统计相关参数信息
 *
 * @author tao
 */
@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VStatistics {

  //以下数据为X轴选项
  private String time;

  private String userId;

  private Integer consumeTime;


  //以下数据为Y轴选项
  private int count;
}
