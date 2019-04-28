package com.zhuxun.dc.apirunner.entity.vo.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dc.apirunner.dao.entity.DcApi;
import com.zhuxun.dc.apirunner.entity.VCreteAndModify;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VApi extends VCreteAndModify {

  String id;

  String apiName;

  String apiOperation;

  String apiSummary;

  String apiPath;

  String apiProtocol;

  String apiType;

  String projectId;

  String folderId;

  Byte status;

  private List<VParam> apiParamList;

  private VResponse apiResponse;

  private VApiImpl apiImpl;

  public static VApi of(DcApi dcApi) {
    return Optional.of(dcApi)
        .map(
            api -> {
              VApi vApi = new VApi();
              vApi.setCreteDatetime(api.getCreateDatetime());
              vApi.setCreteUserId(api.getCreateUserid());
              vApi.setModifyDatetime(api.getModifyDatetime());
              vApi.setModifyUserId(api.getModifyUserid());
              vApi.setId(api.getId())
                  .setApiName(api.getApiName())
                  .setApiOperation(api.getApiOperation())
                  .setApiPath(api.getApiPath())
                  .setApiProtocol(api.getApiProtocol())
                  .setApiSummary(api.getApiSummary())
                  .setApiType(api.getApiType())
                  .setFolderId(api.getFolderId())
                  .setProjectId(api.getProjectId())
                  .setStatus(api.getStatus());
              return vApi;
            })
        .orElse(null);
  }

  public static VApi of(
      DcApi dcApi, List<VParam> apiParamList, VResponse apiResponse, VApiImpl apiImpl) {
    return Optional.of(of(dcApi))
        .map(
            api -> {
              api.setApiParamList(apiParamList).setApiResponse(apiResponse).setApiImpl(apiImpl);
              return api;
            })
        .orElse(null);
  }
}
