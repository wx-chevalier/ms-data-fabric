package com.zhuxun.dc.apirunner.controller;

import com.zhuxun.dc.apirunner.entity.vo.VResult;
import com.zhuxun.dc.apirunner.exception.ManagedDatasourceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping(value = "/initialize")
@Api(description = "初始化数据源")
public class DatasourceInitializerResource extends BaseResource {
  @ApiOperation(value = "解析SQL语句,提取参数信息", notes = "SQL 方言为 presto 时，无需指定数据源，否则必须指定")
  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public VResult analyzeParameter(@RequestParam(required = false) String dsName)
      throws SQLException, ManagedDatasourceException {
    if (dsName == null) {
      datasourceInitService.initAllDatasource();
    } else {
      datasourceInitService.initDatasource(dsName);
    }
    return new VResult("初始化成功");
  }
}
