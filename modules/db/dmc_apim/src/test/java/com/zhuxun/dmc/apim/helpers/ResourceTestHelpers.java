package com.zhuxun.dmc.apim.helpers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.apim.dto.api.APIDocTO;
import com.zhuxun.dmc.apim.dto.api.apiimpl.APIImplExecution;
import com.zhuxun.dmc.apim.dto.api.apiimpl.APIImplTO;
import com.zhuxun.dmc.apim.dto.api.creation.APIModification;
import com.zhuxun.dmc.apim.dto.environment.EnvironmentDTO;
import com.zhuxun.dmc.apim.dto.project.*;
import com.zhuxun.dmc.apim.dto.token.TokenCreation;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

/**
 * 测试访问 REST 资源的辅助函数
 * <p>
 * 注意，执行资源访问前需要手工设定 objectMapper 和 mockMvc 值
 */
@Accessors(chain = true)
@Slf4j
public class ResourceTestHelpers {
  @Setter
  static ObjectMapper objectMapper;

  @Setter
  static MockMvc mockMvc;

  public static MockHttpServletRequestBuilder withToken(
      MockHttpServletRequestBuilder builder, String token) {
    if (token == null) {
      return builder;
    } else {
      return builder.header("Authorization", token);
    }
  }

  public static <T> T deserialize(ResultActions resultActions, Class<T> clazz) throws IOException {
    String resultStr = resultActions.andReturn()
        .getResponse()
        .getContentAsString();
    log.debug("deserialize {}\n{}", clazz, resultStr);
    return objectMapper.readValue(resultStr, clazz);
  }

  public static <T> T deserialize(ResultActions resultActions, TypeReference<T> t) throws IOException {
    String resultStr = resultActions.andReturn()
        .getResponse()
        .getContentAsString();
    log.debug("deserialize {}\n{}", t, resultStr);
    return objectMapper.readValue(resultStr, t);
  }

  // region project
  public static ResultActions get_project_type(String token) throws Exception {
    return mockMvc.perform(withToken(get("/project/type"), token));
  }

  public static ResultActions get_project_list(String token) throws Exception {
    return mockMvc.perform(withToken(get("/project"), token));
  }

  public static ResultActions get_project_detail(String token, String prjId) throws Exception {
    return mockMvc.perform(withToken(get("/project/{id}", prjId), token));
  }

  public static ResultActions patch_project(String token, String prjId, ProjectModification modification) throws Exception {
    return mockMvc.perform(withToken(patch("/project/{id}", prjId), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(modification)));
  }

  public static ResultActions post_project(String token, ProjectCreation projectCreation) throws Exception {
    return mockMvc.perform(withToken(post("/project"), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(projectCreation)));
  }

  public static ResultActions get_project(String token, String prjId) throws Exception {
    return mockMvc.perform(withToken(get("/project/{id}", prjId), token));
  }

  public static ResultActions get_project_api(String token, String prjId) throws Exception {
    return mockMvc.perform(withToken(get("/project/{id}/api", prjId), token));
  }

  public static ResultActions get_project_folder(String token, String prjId) throws Exception {
    return mockMvc.perform(withToken(get("/project/{id}/folder", prjId), token));
  }

  public static ResultActions get_project_doc(String token, String prjId) throws Exception {
    return mockMvc.perform(withToken(get("/project/{id}/doc", prjId), token));
  }

  public static ResultActions get_project_env(String token, String prjId) throws Exception {
    return mockMvc.perform(withToken(get("/project/{id}/environment", prjId), token));
  }
  // endregion

  // region project doc
  public static ResultActions post_projectdoc(String token, ProjectDocTO projectDocTO) throws Exception {
    return mockMvc.perform(withToken(post("/projectdoc"), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(projectDocTO)));
  }

  public static ResultActions get_projectdoc(String token, String docId) throws Exception {
    return mockMvc.perform(withToken(get("/projectdoc/{id}", docId), token));
  }

  public static ResultActions patch_projectdoc(String token, String docId, ProjectDocTO projectDocTO) throws Exception {
    return mockMvc.perform(withToken(patch("/projectdoc/{id}", docId), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(projectDocTO)));
  }

  public static ResultActions delete_projectdoc(String token, String docId) throws Exception {
    return mockMvc.perform(withToken(delete("/projectdoc/{id}", docId), token));
  }
  // endregion

  // region environment
  public static ResultActions get_environment(String token, String envId) throws Exception {
    return mockMvc.perform(withToken(get("/environment/{envId}", envId), token));
  }

  public static ResultActions post_environment(String token, EnvironmentDTO environmentDTO) throws Exception {
    return mockMvc.perform(withToken(post("/environment"), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(environmentDTO)));
  }

  public static ResultActions patch_environment(String token, String envId, EnvironmentDTO environmentDTO) throws Exception {
    return mockMvc.perform(withToken(patch("/environment/{envId}", envId), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(environmentDTO)));
  }

  public static ResultActions delete_environment(String token, List<String> envIds) throws Exception {
    return mockMvc.perform(withToken(delete("/environment"), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(envIds)));
  }
  // endregion

  // region folder
  public static ResultActions get_folder(String token, String folderId) throws Exception {
    return mockMvc.perform(withToken(get("/folder/{id}", folderId), token));
  }

  public static ResultActions post_folder(String token, FolderCreation folderCreation) throws Exception {
    return mockMvc.perform(withToken(post("/folder"), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(folderCreation)));
  }

  public static ResultActions patch_folder(String token, String folderId, FolderModification modification) throws Exception {
    return mockMvc.perform(withToken(patch("/folder/{folderId}", folderId), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(modification)));
  }

  public static ResultActions delete_folder(String token, String folderId) throws Exception {
    return mockMvc.perform(withToken(delete("/folder/{folderId}", folderId), token));
  }
  // endregion

  // region api
  public static ResultActions post_api(String token, APIModification apiModification) throws Exception {
    return mockMvc.perform(withToken(post("/api"), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(apiModification)));
  }

  public static ResultActions delete_api(String token, String apiId) throws Exception {
    return mockMvc.perform(withToken(delete("/api/{apiId}", apiId), token));
  }

  public static ResultActions get_api(String token, String apiId) throws Exception {
    return mockMvc.perform(withToken(get("/api/{apiId}", apiId), token));
  }

  public static ResultActions patch_api(String token, String apiId, APIModification apiModification) throws Exception {
    return mockMvc.perform(withToken(patch("/api/{apiId}", apiId), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(apiModification)));
  }
  // endregion

  // region api doc
  public static ResultActions get_api_docs_by_apiId(String token, String apiId) throws Exception {
    return mockMvc.perform(withToken(get("/api/{apiId}/docs", apiId), token));
  }

  public static ResultActions get_api_doc(String token, String docId) throws Exception {
    return mockMvc.perform(withToken(get("/apidoc/{docId}", docId), token));
  }

  public static ResultActions post_api_doc(String token, APIDocTO apiDocTO) throws Exception {
    return mockMvc.perform(withToken(post("/apidoc"), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(apiDocTO)));
  }

  public static ResultActions patch_api_doc(String token, String docId, APIDocTO apiDocTO) throws Exception {
    return mockMvc.perform(withToken(patch("/apidoc/{docId}", docId), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(apiDocTO)));
  }

  public static ResultActions delete_api_doc(String token, String docId) throws Exception {
    return mockMvc.perform(withToken(delete("/apidoc/{docId}", docId), token));
  }
  // endregion

  // region api authorization
  public static ResultActions post_auth_user_api(String token, String apiId, Set<String> userIds) throws Exception {
    return mockMvc.perform(withToken(post("/auth/api/{apiId}", apiId), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(userIds)));
  }

  public static ResultActions delete_auth_user_api(String token, String apiId, Set<String> userIds) throws Exception {
    return mockMvc.perform(withToken(delete("/auth/api/{apiId}", apiId), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(userIds)));
  }
  // endregion


  public static ResultActions post_api_token(String token, String apiId, TokenCreation tokenCreation) throws Exception {
    return mockMvc.perform(withToken(post("/api/{apiId}/token", apiId), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(tokenCreation)));
  }

  // region data source
  public static ResultActions get_database(String token) throws Exception {
    return mockMvc.perform(withToken(get("/datasource/database"), token));
  }

  public static ResultActions get_database_table(String token, String datasourceName, String databaseName) throws Exception {
    return mockMvc.perform(withToken(
        get("/datasource/{datasourceName}/database/{databaseName}",
            datasourceName, databaseName),
        token));
  }
  // endregion


  // region api impl
  public static ResultActions post_api_impl_analyze(String token, APIImplTO apiImplTO) throws Exception {
    return mockMvc.perform(withToken(post("/api-dev/analyze"), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(apiImplTO)));
  }

  public static ResultActions post_api_impl_execute(String token, APIImplExecution apiImplExecution) throws Exception {
    return mockMvc.perform(withToken(post("/api-dev/execute"), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(apiImplExecution)));
  }
  // endregion
}
