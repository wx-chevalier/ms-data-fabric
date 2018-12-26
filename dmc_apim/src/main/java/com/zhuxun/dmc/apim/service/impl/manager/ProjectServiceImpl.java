package com.zhuxun.dmc.apim.service.impl.manager;

import com.zhuxun.dmc.apim.config.ApplicationConstants;
import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.project.Project;
import com.zhuxun.dmc.apim.dto.project.ProjectCreation;
import com.zhuxun.dmc.apim.dto.project.ProjectModification;
import com.zhuxun.dmc.apim.repository.model.DcProjectExample;
import com.zhuxun.dmc.apim.repository.model.DcProjectWithBLOBs;
import com.zhuxun.dmc.apim.service.*;
import com.zhuxun.dmc.apim.service.impl.AbstractProjectService;
import com.zhuxun.dmc.apim.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.zhuxun.dmc.apim.config.ApplicationConstants.STATUS_NORMAL;
import static java.util.stream.Collectors.toList;

@Service("managerProjectService")
@Slf4j
public class ProjectServiceImpl extends AbstractProjectService implements ProjectService {
  private ProjectTypeService projectTypeService;

  private FolderService folderService;

  private APIService apiService;

  private ProjectDocService projectDocService;

  @Autowired
  public ProjectServiceImpl(ProjectTypeService projectTypeService,
                            FolderService folderService,
                            APIService apiService,
                            ProjectDocService projectDocService) {
    this.projectTypeService = projectTypeService;
    this.folderService = folderService;
    this.apiService = apiService;
    this.projectDocService = projectDocService;
  }

  @Transactional
  @Override
  public Project createProject(String createUserId, ProjectCreation projectCreation) {
    String id = UUIDUtils.getUUID();
    Date now = new Date();
    dcProjectMapper.insert((DcProjectWithBLOBs)
        projectCreation.toEntity(projectTypeService::getIdByName)
            .withId(id)
            .withCreateUserid(createUserId)
            .withCreateDatetime(now)
            .withStatus(STATUS_NORMAL));
    return getProjectById(id);
  }

  @Transactional
  @Override
  public Project updateProject(String modifyUserId, String projectId, ProjectModification projectModification) {
    dcProjectMapper.updateByPrimaryKeySelective((DcProjectWithBLOBs)
        projectModification.toEntity(projectTypeService::getNameById)
            .withId(projectId)
            .withModifyUserid(modifyUserId)
            .withModifyDatetime(new Date()));
    return getProjectById(projectId);
  }

  @Transactional(readOnly = true)
  @Override
  public Project getProjectById(String projectId) {
    return getProjectById(projectId, true, true, true);
  }

  @Override
  public void logicDeleteProject(String projectId) {
    dcProjectMapper.updateByPrimaryKeySelective((DcProjectWithBLOBs)
        new DcProjectWithBLOBs()
            .withId(projectId)
            .withStatus(ApplicationConstants.STATUS_DISABLE));
  }

  @Override
  public List<Project> getProjectListByUserId(String userId) {
    DcProjectExample projectExample = new DcProjectExample();
    projectExample.createCriteria()
        .andCreateUseridEqualTo(userId)
        .andStatusEqualTo(STATUS_NORMAL);
    return dcProjectMapper.selectByExample(projectExample).stream()
        .map(Project::of)
        .map(p -> p.setApiList(apiService.getAPIListByProjectId(p.getId()).stream()
            .map(api -> new API().setId(api.getId()))
            .collect(toList())))
        .collect(toList());
  }

  /**
   * 通过projectId获取指定的项目信息
   *
   * @param projectId 项目ID
   * @return
   */
  @Transactional(readOnly = true)
  public Project getProjectById(String projectId, Boolean withApi, Boolean withFolder, Boolean withDoc) {
    return Optional.ofNullable(dcProjectMapper.selectByPrimaryKey(projectId))
        .map(p -> p.getStatus() == STATUS_NORMAL ? p : null)
        .map(p -> Project.of(p, projectTypeService::getNameById))
        .map(p -> {
          if (withFolder) {
            p.setFolderList(folderService.getFolderByProjectId(projectId));
          }
          if (withApi) {
            p.setApiList(apiService.getAPIListByProjectId(projectId));
          }
          if (withDoc) {
            p.setDocList(projectDocService.getProjectDocListByProjectId(projectId));
          }
          return p;
        })
        .orElse(null);
  }
}
