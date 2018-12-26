package com.zhuxun.dmc.apim.web.rest;

import com.fasterxml.jackson.core.type.TypeReference;
import com.zhuxun.dmc.apim.domain.datasource.Schema;
import com.zhuxun.dmc.apim.helpers.AbstractIntgrationTest;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static com.zhuxun.dmc.apim.helpers.ResourceTestHelpers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "classpath:db/test/cleanup_db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class DataSourceResourceTest extends AbstractIntgrationTest {

  @Test
  public void test_datasource() throws Exception {
    List<Schema> schemaList = deserialize(
        get_database(managerToken)
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(this.managedDatabaseCount)))
            .andExpect(jsonPath("$[*].name").exists())
            .andExpect(jsonPath("$[*].type").exists())
            .andExpect(jsonPath("$[*].dataSource").exists())
            .andExpect(jsonPath("$[*].dataSource.name").exists())
        ,
        new TypeReference<List<Schema>>() {
        });

    Schema schema = schemaList.get(0);
    get_database_table(managerToken, schema.getDataSource().getName(), schema.getName())
        .andExpect(status().isOk());
  }
}