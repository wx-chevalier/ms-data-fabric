package com.zhuxun.dmc.apim.domain.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.common.Modifiable;
import com.zhuxun.dmc.apim.repository.model.DcProject;
import com.zhuxun.dmc.apim.repository.model.DcProjectWithBLOBs;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Project extends Modifiable<Project> {
  String id;
  String name;
  String description;
  String version;
  String type;

  List<Folder> folderList;
  List<API> apiList;
  List<ProjectDoc> docList;

  public static Project of(DcProject dcProject) {
    return Optional.ofNullable(dcProject)
        .map(
            p ->
                new Project()
                    .setCreateDatetime(p.getCreateDatetime())
                    .setModifyDatetime(p.getModifyDatetime())
                    .setId(p.getId())
                    .setName(p.getProjectName())
                    .setVersion(p.getProjectVersion())
                    .setCreateUser(p.getCreateUserid())
                    .setModifyUser(p.getModifyUserid()))
        .orElse(null);
  }

  public static Project of(DcProjectWithBLOBs dcProject) {
    return Optional.ofNullable(dcProject)
        .map(
            p ->
                new Project()
                    .setCreateDatetime(p.getCreateDatetime())
                    .setModifyDatetime(p.getModifyDatetime())
                    .setId(p.getId())
                    .setName(p.getProjectName())
                    .setDescription(p.getProjectDescription())
                    .setVersion(p.getProjectVersion())
                    .setCreateUser(p.getCreateUserid())
                    .setModifyUser(p.getModifyUserid()))
        .orElse(null);
  }

  public static Project of(DcProjectWithBLOBs dcProject, Function<String, String> getNameByTypeId) {
    return Optional.ofNullable(of(dcProject))
        .map(p -> p.setType(getNameByTypeId.apply(dcProject.getProjectTypeId())))
        .orElse(null);
  }

  public static Project of(DcProject dcProject, Function<String, String> getNameByTypeId) {
    return Optional.ofNullable(of(dcProject))
        .map(p -> p.setType(getNameByTypeId.apply(dcProject.getProjectTypeId())))
        .orElse(null);
  }
}
