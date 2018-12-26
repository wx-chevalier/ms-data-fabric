package com.zhuxun.dmc.zuul.service;

import com.zhuxun.dmc.zuul.repository.model.DcApiStatistics;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 作用：代理接口访问统计接口服务
 *
 * <p>时间：2018/6/27 16:27
 *
 * <p>位置：com.zhuxun.dmc.zuul.service
 *
 * @author Yan - tao
 */

@Service
public class ApiStatisticsService extends AbstractService {

  /**
   * 新增接口执行统计记录
   *
   * @param apiStatistics
   * @return
   */
  @Transactional
  public Integer addApiStatisticsRecord(DcApiStatistics apiStatistics) {
    int count = apiStatisticsMapper.insert(apiStatistics);
    return count;
  }
}
