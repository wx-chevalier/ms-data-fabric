package com.zhuxun.dmc.apim.domain.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dmc.apim.domain.common.Modifiable;
import com.zhuxun.dmc.apim.repository.model.DcProjectDynamic;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectDynamic extends Modifiable<ProjectDynamic> {

  String id;

  String content;

  String type;

  Project project;

  public static ProjectDynamic of(DcProjectDynamic projectDynamic) {
    return Optional.ofNullable(projectDynamic)
        .map(
            dynamic ->
                new ProjectDynamic()
                    .setId(dynamic.getId())
                    .setCreateUser(dynamic.getCreateUser())
                    .setCreateDatetime(dynamic.getCreateDatetime())
                    .setContent(dynamic.getContent())
                    .setType(dynamic.getType()))
        .orElse(null);
  }

  public static ProjectDynamic of(DcProjectDynamic projectDynamic, Project project) {
    return Optional.of(of(projectDynamic).setProject(project)).orElse(null);
  }
}
