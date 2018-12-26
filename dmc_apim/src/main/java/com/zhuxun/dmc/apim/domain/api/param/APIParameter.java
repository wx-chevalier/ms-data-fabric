package com.zhuxun.dmc.apim.domain.api.param;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.apim.repository.model.DcApiParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@Data
@ToString(callSuper = true)
public class APIParameter extends APIParam<APIParameter> {
  @ApiModelProperty("OAS 中 Parameter 对象")
  Parameter parameter;

  @Override
  public ParamType getType() {
    return ParamType.PARAMETER;
  }

  @Override
  public DcApiParam toEntity(ObjectMapper objectMapper) throws JsonProcessingException {
    return super.toEntity(objectMapper)
        .withParamModel(parameter == null
            ? null
            : objectMapper.writeValueAsString(parameter));
  }
}
