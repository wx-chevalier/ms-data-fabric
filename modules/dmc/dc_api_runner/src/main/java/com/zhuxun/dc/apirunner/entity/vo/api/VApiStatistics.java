package com.zhuxun.dc.apirunner.entity.vo.api;

import com.zhuxun.dc.apirunner.dao.entity.DcApiStatistics;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Optional;

/**
 * API 接口详情信息
 *
 * @author tao
 */
@Data
@Accessors(chain = true)
public class VApiStatistics {

  String apiId;

  String userId;

  String apiPath;

  String proxyPath;

  String proxyMethod;

  int consumeTime;

  int statusCode;

  public static VApiStatistics of(DcApiStatistics apiStatistics) {
    return Optional.of(apiStatistics)
        .map(
            statistics -> {
              return new VApiStatistics()
                  .setApiId(statistics.getApiId())
                  .setUserId(statistics.getUserId())
                  .setApiPath(statistics.getApiPath())
                  .setProxyPath(statistics.getProxyPath())
                  .setProxyMethod(statistics.getProxyMethod())
                  .setConsumeTime(statistics.getConsumeTime())
                  .setStatusCode(statistics.getStatusCode());
            })
        .orElse(null);
  }
}
