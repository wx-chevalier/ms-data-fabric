package com.zhuxun.dmc.zuul.web.rest;

import com.zhuxun.dmc.zuul.service.EnvironmentMonitorService;
import org.springframework.beans.factory.annotation.Autowired;

/** @author tao */
public abstract class AbstractResource {

  @Autowired protected EnvironmentMonitorService monitorService;
}
