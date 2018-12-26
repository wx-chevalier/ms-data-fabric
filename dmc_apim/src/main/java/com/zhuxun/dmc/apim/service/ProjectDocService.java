package com.zhuxun.dmc.apim.service;

import com.zhuxun.dmc.apim.domain.project.ProjectDoc;
import com.zhuxun.dmc.apim.dto.project.ProjectDocTO;

import java.util.List;

public interface ProjectDocService {
  ProjectDoc createProjectDoc(String createUserId, ProjectDocTO projectDocTO);

  ProjectDoc getProjectDocById(String docId);

  List<ProjectDoc> getProjectDocListByProjectId(String projectId);

  ProjectDoc updateProjectDocById(String modifyUserId, String docId, ProjectDocTO projectDocTO);

  void logicDeleteProjectDoc(String modifyUserId, String docId);
}
