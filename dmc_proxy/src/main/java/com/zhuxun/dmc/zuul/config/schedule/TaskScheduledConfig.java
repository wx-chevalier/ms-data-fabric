package com.zhuxun.dmc.zuul.config.schedule;

import com.zhuxun.dmc.zuul.service.EnvironmentMonitorService;
import com.zhuxun.dmc.zuul.service.RefreshRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 作用：定时任务调度配置
 *
 * <p>时间：2018/6/27 17:54
 *
 * <p>位置：com.zhuxun.dmc.zuul.config
 *
 * @author Yan - tao
 */
@Component
@Slf4j
public class TaskScheduledConfig {

  @Autowired RouteLocator routeLocator;

  @Autowired RefreshRouteService refreshRouteService;

  @Autowired EnvironmentMonitorService environmentMonitorService;

  /**
   * 定时完成更新Zuul路由规则
   *
   * <p>每隔15s执行一次
   */
  @Scheduled(cron = "0/15 * * * * ?")
  public void scheduledUpdateRouteRules() throws IOException {
      //检测到环境信息发生改变之后，开始将数据映射到DcRoute表中
      environmentMonitorService.addRouteRules();
      refreshRouteService.refreshRoute();
  }
}
