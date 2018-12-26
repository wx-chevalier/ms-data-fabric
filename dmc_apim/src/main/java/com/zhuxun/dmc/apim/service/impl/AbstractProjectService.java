package com.zhuxun.dmc.apim.service.impl;

import com.zhuxun.dmc.apim.domain.project.Project;
import com.zhuxun.dmc.apim.dto.project.ProjectCreation;
import com.zhuxun.dmc.apim.dto.project.ProjectModification;
import com.zhuxun.dmc.apim.service.AbstractService;
import com.zhuxun.dmc.apim.service.ProjectService;
import com.zhuxun.dmc.apim.service.errors.NotSupportedException;

import javax.annotation.Nullable;
import java.util.List;

public abstract class AbstractProjectService extends AbstractService implements ProjectService {
  @Override
  public Project createProject(String createUserId, ProjectCreation projectCreation) {
    throw new NotSupportedException();
  }

  @Nullable
  @Override
  public Project updateProject(String modifyUserId, String projectId, ProjectModification projectModification) {
    throw new NotSupportedException();
  }

  @Nullable
  @Override
  public Project getProjectById(String projectId) {
    throw new NotSupportedException();
  }

  @Override
  public List<Project> getProjectListByUserId(String userId) {
    throw new NotSupportedException();
  }

  @Override
  public void logicDeleteProject(String projectId) {
    throw new NotSupportedException();
  }
}
