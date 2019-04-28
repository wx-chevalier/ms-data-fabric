package com.zhuxun.dmc.apim.web.rest;

import com.zhuxun.dmc.apim.dto.api.apiimpl.APIImplExecution;
import com.zhuxun.dmc.apim.dto.api.apiimpl.SQLImpl;
import com.zhuxun.dmc.apim.helpers.AbstractIntgrationTest;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;
import java.util.Map;

import static com.zhuxun.dmc.apim.helpers.ResourceTestHelpers.post_api_impl_analyze;
import static com.zhuxun.dmc.apim.helpers.ResourceTestHelpers.post_api_impl_execute;
import static com.zhuxun.dmc.apim.helpers.TestData.ds_dmc_api;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "classpath:db/test/cleanup_db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class APIImplDevResourceTest extends AbstractIntgrationTest {

  @Test
  public void test_analyzeSQL() throws Exception {

    post_api_impl_analyze(managerToken,
        new SQLImpl()
            .setDialect("mysql")
            .setDatasourceName(ds_dmc_api)
            .setSql("SELECT table_name AS tableName, column_name AS columnName FROM dc_data_column LIMIT {count: integer}"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.type", is("object")))
        .andExpect(jsonPath("$.properties").exists())
        .andExpect(jsonPath("$.properties.count").exists());
  }

  @Test
  public void test_executeSQL() throws Exception {
    Map<String, Object> param = new HashMap<>();
    param.put("count", 5);

    System.out.println(
        post_api_impl_execute(managerToken,
            new APIImplExecution()
                .setImpl(
                    new SQLImpl()
                        .setDialect("mysql")
                        .setDatasourceName(ds_dmc_api)
                        .setSql("SELECT table_name AS tableName, column_name AS columnName FROM dc_data_column LIMIT {count: integer}"))
                .setParam(param))
            .andExpect(status().isOk())
            .andReturn().getResponse().getContentAsString());
  }
}