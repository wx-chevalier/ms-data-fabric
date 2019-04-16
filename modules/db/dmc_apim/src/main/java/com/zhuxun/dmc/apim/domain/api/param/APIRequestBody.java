package com.zhuxun.dmc.apim.domain.api.param;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.apim.repository.model.DcApiParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.models.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@ToString(callSuper = true)
public class APIRequestBody extends APIParam<APIRequestBody> {
  @ApiModelProperty("OAS 中的 Schema 对象")
  Schema schema;

  @Override
  public ParamType getType() {
    return ParamType.REQUEST_BODY;
  }

  @Override
  public DcApiParam toEntity(ObjectMapper objectMapper) throws JsonProcessingException {
    return super.toEntity(objectMapper)
        .withParamModel(schema == null
            ? null
            : objectMapper.writeValueAsString(schema));
  }
}
