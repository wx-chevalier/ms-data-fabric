package com.zhuxun.dc.apirunner.zuulapi;

import com.zhuxun.dc.apirunner.dao.entity.DcRoute;
import com.zhuxun.dc.apirunner.entity.vo.VZuulRoute;
import com.zhuxun.dc.apirunner.service.RouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.util.StringUtils;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mysql.cj.core.util.StringUtils.isNullOrEmpty;

/**
 * 自定义路由配置
 *
 * @author tao
 */
@Slf4j
public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

  @Autowired private RouteService routeService;

  private ZuulProperties properties;

  public CustomRouteLocator(String servletPath, ZuulProperties properties) {
    super(servletPath, properties);
    this.properties = properties;
    log.info("servletPath:{}", servletPath);
  }

  @Override
  public void refresh() {
    super.doRefresh();
  }

  @Override
  protected Map<String, ZuulRoute> locateRoutes() {
    LinkedHashMap<String, ZuulRoute> routesMap = new LinkedHashMap<String, ZuulRoute>();
    // 从application.properties中加载路由信息
    routesMap.putAll(super.locateRoutes());
    // 从db中加载路由信息
    routesMap.putAll(locateRoutesFromDB());
    // 优化一下配置
    LinkedHashMap<String, ZuulRoute> values = new LinkedHashMap<>();
    for (Map.Entry<String, ZuulRoute> entry : routesMap.entrySet()) {
      String path = entry.getKey();
      // Prepend with slash if not already present.
      if (!path.startsWith("/")) {
        path = "/" + path;
      }
      if (StringUtils.hasText(this.properties.getPrefix())) {
        path = this.properties.getPrefix() + path;
        if (!path.startsWith("/")) {
          path = "/" + path;
        }
      }
      values.put(path, entry.getValue());
    }
    return values;
  }

  /**
   * 从数据库中获取配置信息，此处修改为使用ORM框架
   *
   * @return
   */
  private Map<String, ZuulRoute> locateRoutesFromDB() {
    // 首先更新映射表
    log.info("开始更新Zuul配置");
    List<DcRoute> dcRoutes = routeService.updateApiRoute();

    Map<String, ZuulRoute> routes = new LinkedHashMap<>();
    List<VZuulRoute> results =
        dcRoutes
            .stream()
            .map(
                dcRoute -> {
                  VZuulRoute vZuulRoute = new VZuulRoute();
                  vZuulRoute.setId(dcRoute.getId());
                  vZuulRoute.setStripPrefix(dcRoute.getStripPrefix());
                  vZuulRoute.setPath(dcRoute.getPath());
                  vZuulRoute.setRetryable(dcRoute.getRetryable());
                  vZuulRoute.setUrl(dcRoute.getUrl());
                  return vZuulRoute;
                })
            .collect(Collectors.toList());
    for (VZuulRoute result : results) {
      if (isNullOrEmpty(result.getPath()) || isNullOrEmpty(result.getUrl())) {
        continue;
      }
      ZuulRoute zuulRoute = new ZuulRoute();
      try {
        BeanUtils.copyProperties(result, zuulRoute);
      } catch (Exception e) {
        log.error("更新Zuul代理参数出现异常 ", e);
      }
      routes.put(zuulRoute.getPath(), zuulRoute);
    }
    return routes;
  }
}
