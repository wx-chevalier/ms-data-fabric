package com.zhuxun.dmc.apim.domain.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.apim.domain.common.Identifiable;
import com.zhuxun.dmc.apim.repository.model.DcApiResponse;
import io.swagger.v3.oas.models.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.IOException;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIResponse extends Identifiable<APIResponse> {
  Schema model;

  public DcApiResponse toEntity(ObjectMapper objectMapper) throws JsonProcessingException {
    return new DcApiResponse()
        .withId(id)
        .withResponseModel(objectMapper.writeValueAsString(model));
  }

  public static APIResponse of(
      ObjectMapper objectMapper, DcApiResponse dcApiResponse)
      throws IOException {

    if (dcApiResponse == null) {
      return null;
    }
    return new APIResponse()
        .setId(dcApiResponse.getId())
        .setModel(objectMapper.readValue(
            dcApiResponse.getResponseModel(),
            Schema.class));
  }
}
