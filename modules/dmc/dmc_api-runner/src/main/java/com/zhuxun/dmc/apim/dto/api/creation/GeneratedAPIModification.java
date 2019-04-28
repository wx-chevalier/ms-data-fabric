package com.zhuxun.dmc.apim.dto.api.creation;

import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.api.apiimpl.APIImplDef;
import com.zhuxun.dmc.apim.repository.model.DcApi;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@ToString(callSuper = true)
@ApiModel(parent = APIModification.class)
public class GeneratedAPIModification extends APIModification<GeneratedAPIModification> {
  private APIImplDef impl;

  @Override
  public API.APIType getType() {
    return API.APIType.GENERATED;
  }

  @Override
  public DcApi toEntity() {
    return super.toEntity().withApiOperation("POST").withApiProtocol("http");
  }
}
