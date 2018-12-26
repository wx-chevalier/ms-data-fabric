package com.zhuxun.dmc.apim.domain.api;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dmc.apim.domain.api.apiimpl.APIImpl;
import com.zhuxun.dmc.apim.domain.api.param.APIParam;
import com.zhuxun.dmc.apim.domain.api.param.APIRequestBody;
import com.zhuxun.dmc.apim.domain.common.Modifiable;
import com.zhuxun.dmc.apim.domain.project.Folder;
import com.zhuxun.dmc.apim.domain.project.Project;
import com.zhuxun.dmc.apim.domain.sec.User;
import com.zhuxun.dmc.apim.repository.model.DcApi;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;

/** 接口详情 */
@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class API extends Modifiable<API> {
  String id;
  String name;
  String path;
  String protocol;
  String summary;
  String operation;
  APIType type;

  Project project;
  Folder folder;

  List<APIParam> param;
  APIResponse response;
  List<APIDoc> doc;
  APIImpl impl;

  /** 被授权使用的企业用户列表 */
  List<User> authedUserList;

  @JsonIgnore
  public APIRequestBody getRequestBody() {
    for (APIParam apiParam : param) {
      if (apiParam instanceof APIRequestBody) {
        return (APIRequestBody) apiParam;
      }
    }
    return null;
  }

  public static API of(DcApi dcApi) {
    return Optional.ofNullable(dcApi)
        .map(
            m ->
                new API()
                    .setCreateDatetime(m.getCreateDatetime())
                    .setModifyDatetime(m.getModifyDatetime())
                    .setId(m.getId())
                    .setProject(new Project().setId(m.getProjectId()))
                    .setFolder(
                        Optional.ofNullable(m.getFolderId())
                            .map(folderId -> new Folder().setId(folderId))
                            .orElse(null))
                    .setName(m.getApiName())
                    .setPath(m.getApiPath())
                    .setProtocol(m.getApiProtocol())
                    .setSummary(m.getApiSummary())
                    .setOperation(m.getApiOperation())
                    .setType(APIType.fromString(m.getApiType())))
        .orElse(null);
  }

  public enum APIType {
    PROXY,
    GENERATED;

    public static APIType fromString(String typ) {
      switch (typ) {
        case "PROXY":
          return PROXY;
        case "GENERATED":
          return GENERATED;
        default:
          return PROXY;
      }
    }
  }
}
