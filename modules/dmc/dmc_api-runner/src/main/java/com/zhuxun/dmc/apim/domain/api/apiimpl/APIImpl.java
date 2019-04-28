package com.zhuxun.dmc.apim.domain.api.apiimpl;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.apim.repository.model.DcApiImpl;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.IOException;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIImpl {
  String id;

  APIImplDef impl;

  @JsonIgnore
  public APIImpl setImpl(ObjectMapper objectMapper, String impl) throws IOException {
    this.impl = objectMapper.readValue(impl, APIImplDef.class);
    return this;
  }

  public static APIImpl of(ObjectMapper objectMapper, DcApiImpl dcApiImpl) throws IOException {
    if (dcApiImpl == null) {
      return null;
    }
    return new APIImpl().setId(dcApiImpl.getId()).setImpl(objectMapper, dcApiImpl.getApiImpl());
  }
}
