package com.zhuxun.dmc.zuul.web.zuul;

import com.google.common.base.Strings;
import com.zhuxun.dmc.zuul.domain.zuul.VZuulRoute;
import com.zhuxun.dmc.zuul.repository.model.DcRoute;
import com.zhuxun.dmc.zuul.service.DynamicRouteService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.RefreshableRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.SimpleRouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties.ZuulRoute;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 自定义路由配置
 *
 * @author tao
 */
@Slf4j
public class CustomRouteLocator extends SimpleRouteLocator implements RefreshableRouteLocator {

  @Autowired
  private DynamicRouteService routeService;

  private ZuulProperties properties;

  private  static HashSet<String> sensitiveKey;

  static {
    sensitiveKey = new HashSet<>();
    sensitiveKey.add("Access-Control-Allow-Headers");
    sensitiveKey.add("Access-Control-Allow-Methods");
    sensitiveKey.add("Access-Control-Allow-Origin");
    sensitiveKey.add("Access-Control-Max-Age");
  }

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
    List<DcRoute> dcRoutes = routeService.getAllRouteRules();

    Map<String, ZuulRoute> routes = new LinkedHashMap<>();
    List<VZuulRoute> results =
        dcRoutes
            .stream()
            .map(
                dcRoute -> {
                  VZuulRoute vZuulRoute =
                      new VZuulRoute()
                          .setId(dcRoute.getId())
                          .setStripPrefix(dcRoute.getStripPrefix())
                          .setPath(dcRoute.getPath())
                          .setRetryable(dcRoute.getRetryable())
                          .setUrl(dcRoute.getUrl());
                  return vZuulRoute;
                })
            .collect(Collectors.toList());
    for (VZuulRoute result : results) {
      if (Strings.isNullOrEmpty(result.getPath()) || Strings.isNullOrEmpty(result.getUrl())) {
        continue;
      }
      ZuulRoute zuulRoute = new ZuulRoute();
      try {
        BeanUtils.copyProperties(result, zuulRoute);
      } catch (Exception e) {
        log.error("更新Zuul代理参数出现异常 ", e);
      }
      zuulRoute.setSensitiveHeaders(sensitiveKey);
      routes.put(zuulRoute.getPath(), zuulRoute);
    }
    return routes;
  }
}
