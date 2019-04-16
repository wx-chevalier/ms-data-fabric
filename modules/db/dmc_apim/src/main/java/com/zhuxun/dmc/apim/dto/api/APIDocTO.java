package com.zhuxun.dmc.apim.dto.api;

import com.zhuxun.dmc.apim.dto.DocTO;
import com.zhuxun.dmc.apim.repository.model.DcApiDoc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class APIDocTO extends DocTO {
  // 文档所属api
  private String apiId;

  public DcApiDoc toEntity() {
    return new DcApiDoc()
        .withApiId(apiId)
        .withTitle(title)
        .withContentType(contentType)
        .withContent(content);
  }
}
