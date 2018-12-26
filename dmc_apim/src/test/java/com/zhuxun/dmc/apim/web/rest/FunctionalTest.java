package com.zhuxun.dmc.apim.web.rest;


import com.fasterxml.jackson.core.type.TypeReference;
import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.api.APIDoc;
import com.zhuxun.dmc.apim.domain.api.param.APIParameter;
import com.zhuxun.dmc.apim.domain.api.param.APIRequestBody;
import com.zhuxun.dmc.apim.domain.environment.Environment;
import com.zhuxun.dmc.apim.domain.project.Folder;
import com.zhuxun.dmc.apim.domain.project.Project;
import com.zhuxun.dmc.apim.domain.project.ProjectDoc;
import com.zhuxun.dmc.apim.dto.api.APIDocTO;
import com.zhuxun.dmc.apim.dto.api.creation.GeneratedAPIModification;
import com.zhuxun.dmc.apim.dto.api.creation.ProxyAPIModification;
import com.zhuxun.dmc.apim.dto.environment.EnvironmentDTO;
import com.zhuxun.dmc.apim.dto.project.*;
import com.zhuxun.dmc.apim.dto.token.TokenCreation;
import com.zhuxun.dmc.apim.helpers.AbstractIntgrationTest;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.junit.Test;
import org.springframework.test.context.jdbc.Sql;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static com.zhuxun.dmc.apim.helpers.ResourceTestHelpers.*;
import static com.zhuxun.dmc.apim.helpers.TestData.generated_sql_api_creation;
import static com.zhuxun.dmc.apim.helpers.TestData.proxy_api_creation;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Sql(value = "classpath:db/test/cleanup_db.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FunctionalTest extends AbstractIntgrationTest {

  @Test
  @SuppressWarnings("unchecked")
  public void test_manager_function() throws Exception {
    // ----------- MANAGER ------------
    // 获取项目列表
    get_project_list(managerToken).andExpect(jsonPath("$", hasSize(0)));

    // === 进入项目创建页面

    // 选中项目类型
    String typName = deserialize(
        get_project_type(managerToken)
            .andExpect(jsonPath("$", hasSize(5))),
        new TypeReference<List<String>>() {
        }).get(0);

    // 创建一个项目
    Project prjHello = deserialize(
        post_project(managerToken, new ProjectCreation()
            .setName("hello")
            .setType(typName)
            .setVersion("1.0")
            .setDescription("desc"))
            .andExpect(jsonPath("$.type", is(typName))),
        Project.class);
    assertNotNull(prjHello.getId());

    // === 创建完，进入项目详情页
    get_project_detail(managerToken, prjHello.getId())
        .andExpect(jsonPath("$.type", is(typName)));

    get_project_list(managerToken).andExpect(jsonPath("$", hasSize(1)));

    // 更新项目信息
    patch_project(managerToken, prjHello.getId(),
        (ProjectModification) new ProjectModification()
            .setName("world")
            .setDescription("new desc"))
        .andExpect(jsonPath("$.type", is(typName)))
        .andExpect(jsonPath("$.description", is("new desc")))
        .andExpect(jsonPath("$.id", is(prjHello.getId())));

    // region === 查看一下项目下的目录、环境、文档、接口
    get_project_api(managerToken, prjHello.getId()).andExpect(jsonPath("$", hasSize(0)));
    get_project_folder(managerToken, prjHello.getId()).andExpect(jsonPath("$", hasSize(0)));
    get_project_env(managerToken, prjHello.getId()).andExpect(status().is2xxSuccessful()).andExpect(jsonPath("$", hasSize(0)));
    get_project_doc(managerToken, prjHello.getId()).andExpect(jsonPath("$", hasSize(0)));
    // endregion

    // region === 操作一下目录

    // 创建两个目录
    Folder folder1 = deserialize(
        post_folder(managerToken, new FolderCreation()
            .setProjectId(prjHello.getId()).setName("目录 1").setDescription("desc"))
            .andExpect(jsonPath("$.description", is("desc")))
            .andExpect(jsonPath("$.name", is("目录 1"))),
        Folder.class);
    Folder folder2 = deserialize(
        post_folder(managerToken, new FolderCreation()
            .setProjectId(prjHello.getId()).setName("目录 2").setDescription("desc"))
            .andExpect(jsonPath("$.description", is("desc")))
            .andExpect(jsonPath("$.name", is("目录 2"))),
        Folder.class);
    get_project_folder(managerToken, prjHello.getId()).andExpect(jsonPath("$", hasSize(2)));

    // 删除一个目录
    delete_folder(managerToken, folder2.getId());
    get_project_folder(managerToken, prjHello.getId()).andExpect(jsonPath("$", hasSize(1)));

    // 更新一个目录
    patch_folder(managerToken, folder1.getId(),
        (FolderModification) new FolderModification().setName("目录 1 重命名"));

    // 获取目录详情
    get_folder(managerToken, folder1.getId())
        .andExpect(jsonPath("$.name", is("目录 1 重命名")));

    // endregion

    // region === 进入文档页面

    // 创建两份文档
    ProjectDoc doc1 = deserialize(
        post_projectdoc(managerToken,
            (ProjectDocTO) new ProjectDocTO()
                .setProjectId(prjHello.getId())
                .setTitle("项目文档 1")
                .setContentType("MARKDOWN")
                .setContent("1. hello\n2. world"))
            .andExpect(jsonPath("$.title", is("项目文档 1"))),
        ProjectDoc.class);
    ProjectDoc doc2 = deserialize(
        post_projectdoc(managerToken,
            (ProjectDocTO) new ProjectDocTO()
                .setProjectId(prjHello.getId())
                .setTitle("项目文档 2")
                .setContentType("MARKDOWN")
                .setContent("1. hello\n2. world"))
            .andExpect(jsonPath("$.title", is("项目文档 2"))),
        ProjectDoc.class);
    assertNotNull(doc1.getId());
    assertNotNull(doc2.getId());

    get_project_doc(managerToken, prjHello.getId())
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].createUser").exists());

    // 获取该文档信息
    get_projectdoc(managerToken, doc1.getId())
        .andExpect(jsonPath("$.title", is("项目文档 1")))
        .andExpect(jsonPath("$.content", is("1. hello\n2. world")));

    // 更新文档信息
    patch_projectdoc(managerToken, doc1.getId(),
        (ProjectDocTO) new ProjectDocTO().setContent("# to be done"))
        .andExpect(jsonPath("$.content", is("# to be done")));

    // 删除项目文档 2
    delete_projectdoc(managerToken, doc2.getId());
    get_project_doc(managerToken, prjHello.getId()).andExpect(jsonPath("$", hasSize(1)));
    // endregion

    // region === 进入环境页面

    // 创建 3 个环境
    Environment env1 = deserialize(post_environment(managerToken, new EnvironmentDTO()
            .setProjectId(prjHello.getId())
            .setEnvName("测试环境 1").setEnvValue("{\"k1\": \"v1\"}"))
            .andExpect(jsonPath("$.envName", is("测试环境 1")))
            .andExpect(jsonPath("$.envValue", is("{\"k1\": \"v1\"}"))),
        Environment.class);
    Environment env2 = deserialize(post_environment(managerToken, new EnvironmentDTO()
            .setProjectId(prjHello.getId())
            .setEnvName("测试环境 2").setEnvValue("{\"k1\": \"v1\"}")),
        Environment.class);
    Environment env3 = deserialize(post_environment(managerToken, new EnvironmentDTO()
            .setProjectId(prjHello.getId())
            .setEnvName("测试环境 3").setEnvValue("{\"k1\": \"v1\"}")),
        Environment.class);
    get_project_env(managerToken, prjHello.getId()).andExpect(jsonPath("$", hasSize(3)));

    // 删除 2 个环境
    delete_environment(managerToken, Arrays.asList(env2.getId(), env3.getId()));
    get_project_env(managerToken, prjHello.getId()).andExpect(jsonPath("$", hasSize(1)));

    // 更新环境值
    patch_environment(managerToken, env1.getId(),
        new EnvironmentDTO().setEnvValue("{\"k1\": \"v2\"}"));

    // 获取环境详情
    get_environment(managerToken, env1.getId())
        .andExpect(jsonPath("$.envValue", is("{\"k1\": \"v2\"}")));

    // endregion

    // region === 创建一些接口
    API api1 = deserialize(
        post_api(managerToken, proxy_api_creation.setFolderId(folder1.getId()).setProjectId(prjHello.getId())), API.class);
    API api2 = deserialize(
        post_api(managerToken, generated_sql_api_creation.setFolderId(folder1.getId()).setProjectId(prjHello.getId())), API.class);
    API api3 = deserialize(
        post_api(managerToken, generated_sql_api_creation.setName("api 3").setFolderId(null).setProjectId(prjHello.getId())), API.class);

    get_project_api(managerToken, prjHello.getId()).andExpect(jsonPath("$", hasSize(3)));
    // 更新一个接口
    patch_api(managerToken, api2.getId(), new GeneratedAPIModification().setName("接口 2 重命名"))
        .andExpect(status().is2xxSuccessful());
    get_api(managerToken, api2.getId())
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.name", is("接口 2 重命名")));
    // 删除一个接口
    delete_api(managerToken, api2.getId()).andExpect(status().is2xxSuccessful());
    get_project_api(managerToken, prjHello.getId()).andExpect(jsonPath("$", hasSize(2)));

    // 接口详情包含：Param, Response
    get_api(managerToken, api1.getId())
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.response", is(notNullValue())))
        .andExpect(jsonPath("$.response.id").exists())
        .andExpect(jsonPath("$.response.model").exists())
        .andExpect(jsonPath("$.param", hasSize(2)))
        .andExpect(jsonPath("$.param[*].id").exists())
        .andExpect(jsonPath("$.param[*].schema").exists())
        .andExpect(jsonPath("$.param[*].schema", hasSize(1)))
        .andExpect(jsonPath("$.param[*].parameter").exists())
        .andExpect(jsonPath("$.param[*].parameter", hasSize(1)))
        .andExpect(jsonPath("$.param[*].type").exists())
        .andExpect(jsonPath("$.param[*].type", hasItems("REQUEST_BODY", "PARAMETER")));

    // 更新 Param
    patch_api(managerToken, api1.getId(), new ProxyAPIModification()
        .setParameters(Arrays.asList(
            new APIRequestBody()
                .setSchema(new StringSchema()),
            new APIParameter()
                .setParameter(new Parameter()))))
        .andExpect(status().is2xxSuccessful());
    get_api(managerToken, api1.getId())
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.param", hasSize(2)))
        .andExpect(jsonPath("$.param[*].id").exists())
        .andExpect(jsonPath("$.param[*].schema").exists())
        .andExpect(jsonPath("$.param[*].schema", hasSize(1)))
        .andExpect(jsonPath("$.param[*].parameter").exists())
        .andExpect(jsonPath("$.param[*].parameter", hasSize(1)))
        .andExpect(jsonPath("$.param[*].type").exists())
        .andExpect(jsonPath("$.param[*].type", hasItems("REQUEST_BODY", "PARAMETER")));

    // 更新 Response
    patch_api(
        managerToken,
        api1.getId(),
        new ProxyAPIModification().setResponse(new StringSchema()))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.response", is(notNullValue())))
        .andExpect(jsonPath("$.response.model").exists());
    // endregion

    // region === 创建接口文档
    get_api_docs_by_apiId(managerToken, api1.getId())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(0)));

    // 为接口 1 创建两文档
    APIDoc apiDoc1 = deserialize(
        post_api_doc(managerToken, (APIDocTO) new APIDocTO()
            .setApiId(api1.getId())
            .setContent("hello world")
            .setContentType("TEXT")
            .setTitle("first doc")),
        APIDoc.class);

    APIDoc apiDoc2 = deserialize(
        post_api_doc(managerToken, (APIDocTO) new APIDocTO()
            .setApiId(api1.getId())
            .setContent("hello world")
            .setContentType("TEXT")
            .setTitle("second doc")),
        APIDoc.class);

    get_api_docs_by_apiId(managerToken, api1.getId())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(2)));

    // 查看一个文档
    get_api_doc(managerToken, apiDoc2.getId())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", is("hello world")))
        .andExpect(jsonPath("$.type", is("TEXT")))
        .andExpect(jsonPath("$.title", is("second doc")));

    // 删除一个文档
    delete_api_doc(managerToken, apiDoc2.getId());
    get_api_docs_by_apiId(managerToken, api1.getId())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)));

    // 更新一下文档
    patch_api_doc(managerToken, apiDoc1.getId(),
        (APIDocTO) new APIDocTO().setTitle("it sucks")
            .setContent("but it will be better"));
    // 重新查看一下刚才编辑的文档
    get_api_doc(managerToken, apiDoc1.getId())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content", is("but it will be better")))
        .andExpect(jsonPath("$.type", is("TEXT")))
        .andExpect(jsonPath("$.title", is("it sucks")));
    // endregion

    // ----------- CLIENT ------------
    // 项目列表为空
    get_project_list(qy1Token).andExpect(jsonPath("$", hasSize(0)));
    get_project_list(qy2Token).andExpect(jsonPath("$", hasSize(0)));

    // 授权给 qy1 & qy2
    post_auth_user_api(managerToken, api1.getId(), new HashSet(Arrays.asList(qy1Id, qy2Id)))
        .andExpect(status().is2xxSuccessful());

    get_project_list(qy1Token).andExpect(jsonPath("$", hasSize(1)));
    get_project_list(qy2Token).andExpect(jsonPath("$", hasSize(1)));

    // 移除 qy2 的授权
    delete_auth_user_api(managerToken, api1.getId(), new HashSet(Collections.singletonList(qy2Id)))
        .andExpect(status().is2xxSuccessful());

    get_project_list(qy1Token).andExpect(jsonPath("$", hasSize(1)));
    get_project_list(qy2Token).andExpect(jsonPath("$", hasSize(0)));

    // 获取 api1 的访问 token
    post_api_token(qy1Token, api1.getId(), new TokenCreation().setClientClaim("hello world"))
        .andExpect(status().is2xxSuccessful())
        .andExpect(jsonPath("$.apiURL").exists())
        .andExpect(jsonPath("$.token").exists());
  }
}
