package com.zhuxun.dmc.apim.domain.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dmc.apim.domain.common.Modifiable;
import com.zhuxun.dmc.apim.repository.model.DcApiDoc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class APIDoc extends Modifiable<APIDoc> {
  String id;

  String title;

  String content;

  String type;

  Byte status;

  @JsonInclude(JsonInclude.Include.NON_NULL)
  API api;

  public static APIDoc of(DcApiDoc apiDoc) {
    return Optional.ofNullable(apiDoc)
        .map(
            doc ->
                new APIDoc()
                    .setCreateUser(apiDoc.getCreateUserid())
                    .setModifyUser(apiDoc.getModifyUserid())
                    .setCreateDatetime(doc.getCreateDatetime())
                    .setModifyDatetime(doc.getModifyDatetime())
                    .setId(apiDoc.getId())
                    .setTitle(apiDoc.getTitle())
                    .setContent(apiDoc.getContent())
                    .setType(apiDoc.getContentType())
                    .setStatus(apiDoc.getStatus()))
        .orElse(null);
  }
}
