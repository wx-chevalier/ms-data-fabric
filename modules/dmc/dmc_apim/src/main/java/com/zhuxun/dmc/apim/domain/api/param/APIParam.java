package com.zhuxun.dmc.apim.domain.api.param;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.apim.domain.common.Modifiable;
import com.zhuxun.dmc.apim.repository.model.DcApiParam;
import com.zhuxun.dmc.apim.service.errors.NotSupportedException;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = APIParameter.class, name = "PARAMETER"),
    @JsonSubTypes.Type(value = APIRequestBody.class, name = "REQUEST_BODY")})
@JsonIgnoreProperties(value = {"type"}, ignoreUnknown = true)
@ToString(callSuper = true)
public abstract class APIParam<T extends APIParam<T>> extends Modifiable<T> {
  @ApiModelProperty(notes = "接口参数包含两大类：\n" +
      "- REQUEST_BODY [APIRequestBody](#model-APIRequestBody)\n" +
      "- PARAMETER [APIParameter](#model-APIParameter)")
  public abstract ParamType getType();

  public enum ParamType {
    REQUEST_BODY,
    PARAMETER
  }

  public DcApiParam toEntity(ObjectMapper objectMapper)
      throws JsonProcessingException {

    return new DcApiParam()
        .withParamType(getType().toString());
  }

  public static APIParam of(DcApiParam dcApiParam, ObjectMapper objectMapper)
      throws IOException {
    ParamType type = ParamType.valueOf(dcApiParam.getParamType());
    switch (type) {
      case REQUEST_BODY:
        return new APIRequestBody()
            .setId(dcApiParam.getId())
            .setSchema(objectMapper.readValue(
                dcApiParam.getParamModel(), Schema.class));
      case PARAMETER:
        return new APIParameter()
            .setId(dcApiParam.getId())
            .setParameter(objectMapper.readValue(
                dcApiParam.getParamModel(), Parameter.class));
      default:
        throw new NotSupportedException(
            "Unknown param type: " + dcApiParam.getParamType());
    }
  }
}
