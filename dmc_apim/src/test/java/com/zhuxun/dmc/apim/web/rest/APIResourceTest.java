package com.zhuxun.dmc.apim.web.rest;

import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.api.param.APIParam;
import com.zhuxun.dmc.apim.domain.api.param.APIParameter;
import com.zhuxun.dmc.apim.domain.api.param.APIRequestBody;
import com.zhuxun.dmc.apim.helpers.AbstractIntgrationTest;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import static com.zhuxun.dmc.apim.helpers.ResourceTestHelpers.*;
import static com.zhuxun.dmc.apim.helpers.TestData.*;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "classpath:db/test/cleanup_db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(value = "classpath:db/test/test_projects.sql")
public class APIResourceTest extends AbstractIntgrationTest {

  private final String FDL_2_ID = "5FB41CB5-C7EE-44F1-999B-8DC3CE87D9B7";

  @Test
  public void test_create_proxy_api_and_get_by_id() throws Exception {
    HeaderParameter headerParameter = new HeaderParameter();
    headerParameter.setName("lc42-token");
    headerParameter.setDescription("Access token");

    API createdAPI = deserialize(
        post_api(managerToken, proxy_api_creation)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.type", is(API.APIType.PROXY.toString())))
            .andExpect(jsonPath("$.operation", is("GET")))
            .andExpect(jsonPath("$.protocol", is("http")))
            .andExpect(jsonPath("$.path", is("{{PROXY}}/hello")))
            // Project Info
            .andExpect(jsonPath("$.project").exists())
            .andExpect(jsonPath("$.project.id", is(PRJ_1_ID)))
            // Folder Info
            .andExpect(jsonPath("$.folder").exists())
            .andExpect(jsonPath("$.folder.id", is(FDL_1_ID)))
            // Param Model
            .andExpect(jsonPath("$.param").exists())
            .andExpect(jsonPath("$.param", hasSize(2)))
            // Response Model
            .andExpect(jsonPath("$.response").exists())
            .andExpect(jsonPath("$.response.model").exists())
            .andExpect(jsonPath("$.response.model.type").exists())
            .andExpect(jsonPath("$.response.model.type", is("string")))
        ,
        API.class);
    for (APIParam param : createdAPI.getParam()) {
      switch (param.getType()) {
        case PARAMETER:
          assertTrue(param instanceof APIParameter);
          assertEquals("lc42-token",
              ((APIParameter) param).getParameter().getName());
          break;
        case REQUEST_BODY:
          assertTrue(param instanceof APIRequestBody);
          assertEquals("integer",
              ((APIRequestBody) param).getSchema().getType());
      }
    }

    createdAPI = deserialize(
        get_api(managerToken, createdAPI.getId())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.type", is(API.APIType.PROXY.toString())))
            .andExpect(jsonPath("$.operation", is("GET")))
            .andExpect(jsonPath("$.protocol", is("http")))
            .andExpect(jsonPath("$.path", is("{{PROXY}}/hello")))
            // Project Info
            .andExpect(jsonPath("$.project").exists())
            .andExpect(jsonPath("$.project.id", is(PRJ_1_ID)))
            // Folder Info
            .andExpect(jsonPath("$.folder").exists())
            .andExpect(jsonPath("$.folder.id", is(FDL_1_ID)))
            // Param Model
            .andExpect(jsonPath("$.param").exists())
            .andExpect(jsonPath("$.param", hasSize(2)))
            // Response Model
            .andExpect(jsonPath("$.response").exists())
            .andExpect(jsonPath("$.response.model").exists())
            .andExpect(jsonPath("$.response.model.type").exists())
            .andExpect(jsonPath("$.response.model.type", is("string")))
        ,
        API.class);
    for (APIParam param : createdAPI.getParam()) {
      switch (param.getType()) {
        case PARAMETER:
          assertTrue(param instanceof APIParameter);
          assertEquals("lc42-token",
              ((APIParameter) param).getParameter().getName());
          break;
        case REQUEST_BODY:
          assertTrue(param instanceof APIRequestBody);
          assertEquals("integer",
              ((APIRequestBody) param).getSchema().getType());
      }
    }
  }

  @Test
  public void test_create_generated_api_and_get_by_id() throws Exception {
    API api = deserialize(
        post_api(managerToken, generated_sql_api_creation)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.type", is(API.APIType.GENERATED.toString())))
            .andExpect(jsonPath("$.operation", is("POST")))
            .andExpect(jsonPath("$.protocol", is("http")))
            // Impl Info
            .andExpect(jsonPath("$.impl").exists())
            // Project Info
            .andExpect(jsonPath("$.project").exists())
            .andExpect(jsonPath("$.project.id", is(PRJ_1_ID)))
            // Folder Info
            .andExpect(jsonPath("$.folder").exists())
            .andExpect(jsonPath("$.folder.id", is(FDL_1_ID)))
            // Param Model
            .andExpect(jsonPath("$.param").exists())
            .andExpect(jsonPath("$.param", hasSize(1)))
            // Response Model
            .andExpect(jsonPath("$.response").exists())
            .andExpect(jsonPath("$.response.model").exists())
            .andExpect(jsonPath("$.response.model.type").exists())
            .andExpect(jsonPath("$.response.model.type", is("object")))
            .andExpect(jsonPath("$.response.model.properties.type").exists())
            .andExpect(jsonPath("$.response.model.properties.id").exists())
        ,
        API.class);
    for (APIParam param : api.getParam()) {
      switch (param.getType()) {
        case REQUEST_BODY:
          assertTrue(param instanceof APIRequestBody);
          APIRequestBody p = (APIRequestBody) param;
          assertEquals("object", p.getSchema().getType());
          assertTrue(p.getSchema().getProperties().containsKey("count"));
          Schema s = (Schema) p.getSchema().getProperties().get("count");
          assertEquals("integer", s.getType());

      }
    }
  }
}