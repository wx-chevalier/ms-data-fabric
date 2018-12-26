package com.zhuxun.dmc.apim.domain.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dmc.apim.domain.common.Modifiable;
import com.zhuxun.dmc.apim.repository.model.DcProjectDoc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDoc extends Modifiable<ProjectDoc> {

  String id;

  String title;

  String content;

  String type;

  Byte status;

  Project project;

  public static ProjectDoc of(DcProjectDoc projectDoc) {
    return Optional.ofNullable(projectDoc)
        .map(
            doc ->
                new ProjectDoc()
                    .setId(doc.getId())
                    .setCreateUser(doc.getCreateUserid())
                    .setModifyUser(doc.getModifyUserid())
                    .setCreateDatetime(doc.getCreateDatetime())
                    .setModifyDatetime(doc.getModifyDatetime())
                    .setTitle(doc.getTitle())
                    .setContent(doc.getContent())
                    .setType(doc.getContentType())
                    .setStatus(doc.getStatus()))
        .orElse(null);
  }

  public static ProjectDoc of(DcProjectDoc projectDoc, Project project) {
    return Optional.of(of(projectDoc).setProject(project)).orElse(null);
  }
}
