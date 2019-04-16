package com.zhuxun.dc.apirunner.zuulapi;

import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.web.ZuulController;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class CustomZuulHandleMapper extends ZuulHandlerMapping {

  public CustomZuulHandleMapper(RouteLocator routeLocator, ZuulController zuul) {
    super(routeLocator, zuul);
  }

  @Override
  protected Object lookupHandler(String urlPath, HttpServletRequest request) throws Exception {
    request.setAttribute("requestURI","");
    return super.lookupHandler(urlPath, request);
  }
}
