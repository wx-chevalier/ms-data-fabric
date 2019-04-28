package com.zhuxun.dmc.apim.service;

import com.zhuxun.dmc.apim.dto.api.apiimpl.SQLImpl;
import com.zhuxun.dmc.apim.service.impl.manager.api.APIImplDevToolImpl;
import com.zhuxun.dmc.sqlapi.datasource.ManagedDatasource;
import com.zhuxun.dmc.sqlapi.datasource.ManagedDatasourceProperties;
import com.zhuxun.dmc.sqlapi.errors.ManagedDatasourceException;
import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.zhuxun.dmc.apim.helpers.TestData.ds_dmc_api;
import static com.zhuxun.dmc.apim.helpers.TestData.initDataSourceConfig;
import static org.junit.Assert.*;

public class APIImplDevToolTest {
  @Rule
  public TemporaryFolder dsConfigDir = new TemporaryFolder();

  private ManagedDatasource managedDatasource;

  @Before
  public void setUp() throws Exception {
    initDataSourceConfig(dsConfigDir.getRoot());
    this.managedDatasource = new ManagedDatasource(new ManagedDatasourceProperties()
        .setDsConfigDir(dsConfigDir.getRoot().toString())
        .setPresto(new DataSourceProperties()));
  }

  @Test
  public void test_parser() throws SQLException, SQLAPIException, ManagedDatasourceException {
    APIImplDevTool parser = new APIImplDevToolImpl(managedDatasource).parse(new SQLImpl()
        .setDialect("mysql")
        .setDatasourceName(ds_dmc_api)
        .setSql("SELECT table_name AS tableName, column_name AS columnName FROM dc_data_column LIMIT {count: integer}"));

    assertTrue(parser.getIsQuery());
    Schema paramSchema = parser.getRequestBodySchema();
    Schema respSchema = parser.getResponseSchema();
    assertNotNull(paramSchema);
    assertNotNull(respSchema);

    assertEquals("object", paramSchema.getType());
    assertEquals(1, paramSchema.getProperties().size());
    assertTrue(paramSchema.getProperties().containsKey("count"));

    assertEquals("object", respSchema.getType());
    assertEquals(2, respSchema.getProperties().size());
    assertTrue(respSchema.getProperties().containsKey("tableName"));
    assertTrue(respSchema.getProperties().containsKey("columnName"));

    Map<String, Object> param = new HashMap<>();
    param.put("count", 2);
    Object result = parser.execute(param);
    System.out.println(result);
  }
}