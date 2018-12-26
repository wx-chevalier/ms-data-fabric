package com.zhuxun.dmc.apim.service;

import com.zhuxun.dmc.apim.domain.project.Project;
import com.zhuxun.dmc.apim.dto.project.ProjectCreation;
import com.zhuxun.dmc.apim.dto.project.ProjectModification;

import javax.annotation.Nullable;
import java.util.List;

public interface ProjectService {

  Project createProject(String createUserId, ProjectCreation projectCreation);

  @Nullable
  Project updateProject(String modifyUserId, String projectId, ProjectModification projectModification);

  @Nullable
  Project getProjectById(String projectId);

  List<Project> getProjectListByUserId(String userId);

  void logicDeleteProject(String projectId);
}
