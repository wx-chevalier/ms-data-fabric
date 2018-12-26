package com.zhuxun.dc.apirunner.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhuxun.dc.apirunner.entity.dto.SqlAnalysis;
import com.zhuxun.dc.apirunner.entity.dto.SqlExecution;
import com.zhuxun.dc.apirunner.exception.ManagedDatasourceException;
import com.zhuxun.dc.apirunner.exception.SQLAPIException;
import com.zhuxun.dc.apirunner.utils.SchemaUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;

@RestController
@RequestMapping(value = "/analyzer")
@Api(description = "SQL分析和测试执行接口")
public class SqlApiAnalyzerResource extends BaseResource {

  /**
   * 分析SQL语句
   *
   * @return
   */
  @ApiOperation(value = "解析SQL语句,提取参数信息", notes = "SQL 方言为 presto 时，无需指定数据源，否则必须指定")
  @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public String analyzeParameter(@RequestBody SqlAnalysis sqlAnalysis)
      throws SQLAPIException, SQLException, JsonProcessingException {
    Schema schema = sqlApiAnalyzerService.analyzeParameter(sqlAnalysis);
    return SchemaUtils.convertSchema(schema);
  }

  /**
   * 测试执行SQL
   *
   * @throws SQLException    执行SQL的时候出现的异常
   * @throws SQLAPIException 执行解析SQL的时候出现的异常信息
   */
  @ApiOperation(value = "测试执行接口", notes = "提供SQL、数据源信息以及参数示例进行测试，更新、插入语句不会被真正提交到数据库")
  @PostMapping(value = "/exec")
  public Object execSqlTest(@RequestBody SqlExecution sqlExecution)
      throws SQLException, SQLAPIException, IOException, ManagedDatasourceException {
    Object execResult = sqlApiAnalyzerService.execute(sqlExecution);
    return execResult;
  }
}
