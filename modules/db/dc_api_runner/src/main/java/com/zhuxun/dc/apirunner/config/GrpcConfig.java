package com.zhuxun.dc.apirunner.config;

import com.zhuxun.dc.apirunner.rpc.RefreshRouteGrpcService;
import com.zhuxun.dc.apirunner.zuulapi.RefreshRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 远程调用RPC相关配置项
 *
 * @author tao
 */
@Configuration
public class GrpcConfig {

  @Autowired private RefreshRouteService refreshRouteService;

  /**
   * 注入RefreshRouteGrpcService 对象
   *
   * @return
   */
  @Bean
  public RefreshRouteGrpcService grpcService() {
    RefreshRouteGrpcService refreshRouteGrpcService = new RefreshRouteGrpcService();
    refreshRouteGrpcService.setRefreshRouteService(refreshRouteService);
    return refreshRouteGrpcService;
  }
}
