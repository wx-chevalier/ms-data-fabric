package com.zhuxun.dmc.apim.web.rest;

import com.zhuxun.dmc.apim.dto.api.apiimpl.APIImplExecution;
import com.zhuxun.dmc.apim.dto.api.apiimpl.APIImplTO;
import com.zhuxun.dmc.apim.service.impl.manager.api.APIImplDevToolImpl;
import com.zhuxun.dmc.sqlapi.datasource.ManagedDatasource;
import com.zhuxun.dmc.sqlapi.errors.ManagedDatasourceException;
import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;

@RestController
@RequestMapping("/api-dev")
@Api(description = "接口实现、调试相关接口")
public class APIImplDevResource {
  private ManagedDatasource managedDatasource;

  @Autowired
  public APIImplDevResource(ManagedDatasource managedDatasource) {
    this.managedDatasource = managedDatasource;
  }

  @PostMapping("/analyze")
  @ApiOperation("解析、验证 SQL，返回其参数 Schema")
  public Schema analyzeSQL(@RequestBody APIImplTO implTO)
      throws SQLException, SQLAPIException, ManagedDatasourceException {

    return new APIImplDevToolImpl(managedDatasource)
        .parse(implTO)
        .getRequestBodySchema();
  }

  @PostMapping("/execute")
  @ApiOperation("解析、验证 SQL，返回其参数 Schema")
  public Object executeSQL(@RequestBody APIImplExecution implExecution)
      throws ManagedDatasourceException, SQLAPIException, SQLException {

    return new APIImplDevToolImpl(managedDatasource)
        .parse(implExecution.getImpl())
        .execute(implExecution.getParam());
  }
}
