package com.zhuxun.dc.apirunner.zuulapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by xujingfeng on 2017/4/1.
 */
@Configuration
public class CustomZuulConfig {

    @Autowired
    ZuulProperties zuulProperties;

    @Autowired
    ServerProperties server;

    @Bean
    public CustomRouteLocator routeLocator() {
        CustomRouteLocator routeLocator = new CustomRouteLocator(this.server.getServlet().getServletPrefix(), this.zuulProperties);
        return routeLocator;
    }

}
