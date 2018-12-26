package com.zhuxun.dmc.apim.service;

import com.zhuxun.dmc.apim.domain.project.ProjectType;

import java.util.List;

public interface ProjectTypeService {
  List<ProjectType> getAllProjectType();

  ProjectType getProjectTypeById(String projectTypeId);

  ProjectType getProjectTypeByName(String name);

  String getIdByName(String typeName);

  String getNameById(String typeId);
}
