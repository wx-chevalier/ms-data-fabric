package com.zhuxun.dmc.apim.web.rest;

import com.zhuxun.dmc.apim.domain.project.Project;
import com.zhuxun.dmc.apim.dto.project.ProjectCreation;
import com.zhuxun.dmc.apim.helpers.AbstractIntgrationTest;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import static com.zhuxun.dmc.apim.helpers.ResourceTestHelpers.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "classpath:db/test/cleanup_db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class ProjectResourceTest extends AbstractIntgrationTest {
  @Test
  public void test_createProject() throws Exception {
    Project prj1 = deserialize(
        post_project(managerToken,
            new ProjectCreation().setName("prj 1")
                .setDescription("prj 1 desc")
                .setType("OTHERS")
                .setVersion("1.0"))
            .andExpect(status().isOk()),
        Project.class);

    get_project_list(managerToken)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].name", is("prj 1")))
        .andExpect(jsonPath("$[0].folderList").doesNotExist())
        .andExpect(jsonPath("$[0].docList").doesNotExist())
        // 项目列表中同时会展示接口数目，添加了接口信息列表（只包含 ID）
        .andExpect(jsonPath("$[0].apiList").exists());

    get_project(managerToken, prj1.getId())
        .andExpect(jsonPath("$.name", is("prj 1")))
        .andExpect(jsonPath("$.type", is("OTHERS")))
        .andExpect(jsonPath("$.folderList").exists())
        .andExpect(jsonPath("$.apiList").exists())
        .andExpect(jsonPath("$.docList").exists());
  }
}