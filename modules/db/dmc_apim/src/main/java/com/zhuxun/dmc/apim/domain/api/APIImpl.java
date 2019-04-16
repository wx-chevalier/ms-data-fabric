package com.zhuxun.dmc.apim.domain.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dmc.apim.repository.model.DcApiImpl;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Optional;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIImpl {
  String id;
  String impl;

  public static APIImpl of(DcApiImpl dcApiImpl) {
    return Optional.of(dcApiImpl)
        .map(impl -> new APIImpl()
            .setId(impl.getId())
            .setImpl(impl.getApiImpl()))
        .orElse(null);
  }
}
