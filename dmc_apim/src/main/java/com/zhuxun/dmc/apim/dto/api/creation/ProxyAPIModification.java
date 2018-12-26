package com.zhuxun.dmc.apim.dto.api.creation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.api.param.APIParam;
import com.zhuxun.dmc.apim.repository.model.DcApi;
import com.zhuxun.dmc.apim.repository.model.DcApiParam;
import com.zhuxun.dmc.apim.repository.model.DcApiResponse;
import io.swagger.annotations.ApiModel;
import io.swagger.v3.oas.models.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@ApiModel(parent = APIModification.class, description = "注意对于响应 Schema, 现在只考虑 2xx 响应切 Media Type 为 application/json 的响应")
@ToString(callSuper = true)
public class ProxyAPIModification extends APIModification<ProxyAPIModification> {
  private String operation;

  private String protocol;

  private String path;

  private Schema response;

  private List<APIParam> parameters;

  @Override
  public API.APIType getType() {
    return API.APIType.PROXY;
  }

  @Override
  public DcApi toEntity() {
    return super.toEntity()
        .withApiOperation(operation)
        .withApiProtocol(protocol)
        .withApiPath(path);
  }

  @Override
  public List<DcApiParam> toDcApiParamList(ObjectMapper objectMapper)
      throws JsonProcessingException {

    if (parameters == null) {
      return null;
    }

    List<DcApiParam> dcParamSchemas = new ArrayList<>();
    for (APIParam parameterSchema : parameters) {
      dcParamSchemas.add(parameterSchema.toEntity(objectMapper));
    }

    return dcParamSchemas;
  }

  @Override
  public DcApiResponse toDcApiResponse(ObjectMapper objectMapper)
      throws JsonProcessingException {

    if (response == null) {
      return null;
    }

    return new DcApiResponse()
        .withResponseModel(objectMapper.writeValueAsString(response));
  }
}
