package com.zhuxun.dc.apirunner.zuulapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

/**
 * 创建一个刷新路由的服务类，用于更新服务
 *
 * @author tao
 */
@Service
public class RefreshRouteService {

  @Autowired ApplicationEventPublisher publisher;

  @Autowired RouteLocator routeLocator;

  public void refreshRoute() {
    RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(routeLocator);
    publisher.publishEvent(routesRefreshedEvent);
  }
}
