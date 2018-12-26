package com.zhuxun.dmc.runner.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.apim.domain.api.apiimpl.SQLImplDef;
import com.zhuxun.dmc.runner.helpers.AbstractIntgrationTest;
import io.swagger.v3.oas.models.media.ObjectSchema;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashMap;

import static com.zhuxun.dmc.runner.helpers.ResourceTestHelpers.post_api;
import static com.zhuxun.dmc.runner.helpers.TestData.ds_dmc_runner;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(
    value = "classpath:db/test/cleanup_db.sql",
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:db/test/test_generated.sql")
public class APIRunnerTest extends AbstractIntgrationTest {
  @Test
  public void test_access() throws Exception {
    HashMap<String, Object> param = new HashMap<>();
    param.put("name", "api0");
    System.out.println(
        post_api(qy1Token, "api0", param)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(1)))
            .andReturn()
            .getResponse()
            .getContentAsString());
  }

  @Test
  public void name() throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    System.out.println(
        objectMapper.writeValueAsString(
            new SQLImplDef()
                .setDatasourceName(ds_dmc_runner)
                .setDialect("mysql")
                .setSql("SELECT id, api_name AS type FROM dc_api LIMIT {count: integer}")));
    ObjectSchema objectSchema = new ObjectSchema();
    System.out.println(objectMapper.writeValueAsString(objectSchema));
  }
}
