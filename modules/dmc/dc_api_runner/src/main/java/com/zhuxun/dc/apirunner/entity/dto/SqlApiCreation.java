package com.zhuxun.dc.apirunner.entity.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zhuxun.dc.apirunner.constant.StatusConstant;
import com.zhuxun.dc.apirunner.dao.entity.DcApi;
import com.zhuxun.dc.apirunner.dao.entity.DcApiImpl;
import com.zhuxun.dc.apirunner.dao.entity.DcApiParam;
import com.zhuxun.dc.apirunner.dao.entity.DcApiResponse;
import com.zhuxun.dc.apirunner.entity.vo.api.VApiImplModel;
import com.zhuxun.dc.apirunner.utils.JacksonUtil;
import com.zhuxun.dc.apirunner.utils.SchemaUtils;
import io.swagger.v3.oas.models.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class SqlApiCreation extends SqlExecution {
  private String projectId;

  private String folderId;

  private String name;

  private String description;

  public DcApi toDcApi() {
    DcApi api = new DcApi();
    Date now = new Date();
    api.setProjectId(projectId);
    api.setFolderId(folderId);
    api.setApiName(name);
    api.setApiSummary(description);
    api.setApiType("自创建接口");
    api.setApiPath("/api");
    api.setApiProtocol("http");
    api.setApiOperation("POST");
    api.setStatus(StatusConstant.STATUS_NORMAL);
    api.setCreateDatetime(now);
    api.setModifyDatetime(now);
    return api;
  }

  public DcApi toDcApi(String id, String createUserId) {
    DcApi api = toDcApi();
    api.setId(id);
    api.setCreateUserid(createUserId);
    return api;
  }

  public DcApiParam toDcApiParam(
      String paramId, String apiId, String createUserId) throws JsonProcessingException {
    DcApiParam param = new DcApiParam();
    Date now = new Date();

    param.setId(paramId);
    param.setApiId(apiId);
    param.setCreateUserid(createUserId);

    param.setCreateDatetime(now);
    param.setModifyDatetime(now);
    param.setParamModel(SchemaUtils.convertSchema(this.parameterSchema));
    param.setParamType("requestBody");
    return param;
  }

  public DcApiImpl toDcApiImpl(String implId, String apiId, String createUserId) {
    DcApiImpl api = new DcApiImpl();
    Date now = new Date();

    api.setId(implId);
    api.setApiId(apiId);
    api.setCreateUserid(createUserId);

    api.setCreateDatetime(now);
    api.setModifyDatetime(now);

    api.setApiImpl(JacksonUtil.toJSon(new VApiImplModel()
        .setTyp("sql")
        .setDatasourceName(datasourceName)
        .setDialect(dialect)
        .setSql(sql)));

    api.setStatus(StatusConstant.STATUS_NORMAL);

    return api;
  }

  public DcApiResponse toDcApiResponse(
      String respId, String apiId, String createUserId, Schema schema) throws JsonProcessingException {
    DcApiResponse response = new DcApiResponse();
    Date now = new Date();
    response.setId(respId);
    response.setApiId(apiId);
    response.setCreateDatetime(now);
    response.setCreateUserid(createUserId);
    response.setResponseModel(SchemaUtils.convertSchema(schema));
    return response;
  }
}
