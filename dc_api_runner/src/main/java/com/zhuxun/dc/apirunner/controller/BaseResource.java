package com.zhuxun.dc.apirunner.controller;

import com.zhuxun.dc.apirunner.service.*;
import com.zhuxun.dc.apirunner.service.datasource.DatasourceInitService;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseResource {

  @Autowired
  protected SqlApiAnalyzerService sqlApiAnalyzerService;

  @Autowired
  protected SqlApiSaveService sqlApiSaveService;

  @Autowired
  protected SqlApiRunService sqlApiRunService;

  @Autowired
  protected DatasourceInitService datasourceInitService;

  @Autowired
  protected RouteService routeService;

  @Autowired
  protected ApiStatisticsService apiStatisticsService;
}
