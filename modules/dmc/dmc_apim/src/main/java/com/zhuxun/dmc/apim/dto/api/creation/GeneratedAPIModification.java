package com.zhuxun.dmc.apim.dto.api.creation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.api.param.APIParam;
import com.zhuxun.dmc.apim.dto.api.apiimpl.APIImplTO;
import com.zhuxun.dmc.apim.repository.model.DcApi;
import com.zhuxun.dmc.apim.repository.model.DcApiParam;
import com.zhuxun.dmc.apim.repository.model.DcApiResponse;
import com.zhuxun.dmc.apim.service.APIImplDevTool;
import com.zhuxun.dmc.sqlapi.errors.ManagedDatasourceException;
import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.models.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@ToString(callSuper = true)
@ApiModel(parent = APIModification.class)
public class GeneratedAPIModification extends APIModification<GeneratedAPIModification> {
  private APIImplTO impl;

  @JsonIgnore
  private APIImplDevTool implParser;

  @Override
  public API.APIType getType() {
    return API.APIType.GENERATED;
  }

  @Override
  public DcApi toEntity() {
    return super.toEntity()
        .withApiOperation("POST")
        .withApiProtocol("http");
  }

  @Override
  public List<DcApiParam> toDcApiParamList(ObjectMapper objectMapper)
      throws JsonProcessingException, SQLException, SQLAPIException, ManagedDatasourceException {
    if (impl == null) {
      return null;
    }

    Schema requestBodySchema = implParser.parse(impl).getRequestBodySchema();

    return Collections.singletonList(new DcApiParam()
        .withParamType(APIParam.ParamType.REQUEST_BODY.toString())
        .withParamModel(objectMapper.writeValueAsString(requestBodySchema)));
  }

  @Override
  public DcApiResponse toDcApiResponse(ObjectMapper objectMapper)
      throws JsonProcessingException, SQLException, SQLAPIException, ManagedDatasourceException {
    if (impl == null) {
      return null;
    }

    Schema responseSchema = implParser.parse(impl).getResponseSchema();

    return new DcApiResponse()
        .withResponseModel(objectMapper.writeValueAsString(responseSchema));
  }

}
