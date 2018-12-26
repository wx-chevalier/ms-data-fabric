package com.zhuxun.dmc.apim.web.rest;

import com.zhuxun.dmc.apim.config.UserConstants;
import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.environment.Environment;
import com.zhuxun.dmc.apim.domain.project.Folder;
import com.zhuxun.dmc.apim.domain.project.Project;
import com.zhuxun.dmc.apim.domain.project.ProjectDoc;
import com.zhuxun.dmc.apim.domain.project.ProjectType;
import com.zhuxun.dmc.apim.dto.project.ProjectCreation;
import com.zhuxun.dmc.apim.dto.project.ProjectModification;
import com.zhuxun.dmc.apim.security.jwt.TokenProvider;
import com.zhuxun.dmc.apim.service.*;
import com.zhuxun.dmc.apim.web.rest.errors.client.NotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

@RestController()
@RequestMapping(value = "/project")
@Api(description = "项目管理相关接口")
@Slf4j
public class ProjectResource extends AbstractResource {
  private ProjectService managerProjectService;

  private ProjectService clientProjectService;

  private APIService apiService;

  private FolderService folderService;

  private ProjectDocService projectDocService;

  private EnvironmentService environmentService;

  private ProjectTypeService projectTypeService;


  @Autowired
  public ProjectResource(
      TokenProvider tokenProvider,
      @Qualifier("managerProjectService") ProjectService managerProjectService,
      @Qualifier("clientProjectService") ProjectService clientProjectService,
      ProjectTypeService projectTypeService,
      ProjectDocService projectDocService,
      EnvironmentService environmentService,
      FolderService folderService,
      APIService apiService) {
    super(tokenProvider);
    this.managerProjectService = managerProjectService;
    this.clientProjectService = clientProjectService;
    this.projectTypeService = projectTypeService;
    this.projectDocService = projectDocService;
    this.environmentService = environmentService;
    this.folderService = folderService;
    this.apiService = apiService;
  }

  @ApiOperation(value = "项目类型列表")
  @GetMapping("/type")
  public List<String> getProjectTypeList() {
    return projectTypeService.getAllProjectType().stream()
        .map(ProjectType::getName)
        .collect(toList());
  }

  @ApiOperation(
      value = "新增项目",
      notes = "- hasRole('MANAGER')" +
          "\n- 必传参数有：projectName、projectTypeId")
  @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize("hasRole('MANAGER')")
  public Project createProject(@RequestBody ProjectCreation projectCreation) {

    return managerProjectService.createProject(
        currentUserOrThrow().getId(), projectCreation);
  }

  @ApiOperation(
      value = "修改项目信息",
      notes = "- hasRole('MANAGER')" +
          "\n- 更新信息选填")
  @PatchMapping(value = "/{projectId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize("hasRole('MANAGER')")
  public Project updateProject(
      @PathVariable("projectId") String projectId,
      @RequestBody ProjectModification projectModification) {

    return managerProjectService.updateProject(
        currentUserOrThrow().getId(),
        projectId,
        projectModification);
  }

  @ApiOperation(
      value = "获取当前登陆用户项目列表",
      notes = "用户ID不必传，通过Token获取" +
          "\n1. 如果是企业用户，返回被授权使用的项目列表" +
          "\n2. 如果是接口管理用户，返回其创建的项目列表")
  @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<Project> getAllProjectByUserId(HttpServletRequest request) {

    if (request.isUserInRole(UserConstants.ROLE_MANAGER)) {
      return managerProjectService.getProjectListByUserId(currentUserOrThrow().getId());
    } else {
      return clientProjectService.getProjectListByUserId(currentUserOrThrow().getId());
    }
  }

  @ApiOperation(value = "逻辑删除指定ID项目", notes = "逻辑删除指定项目并非物理删除，删除后将在项目展示列表不可见")
  @DeleteMapping(value = "/{projectId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @PreAuthorize("hasRole('MANAGER')")
  public void logicDeleteProject(@PathVariable("projectId") String projectId) {

    managerProjectService.logicDeleteProject(projectId);
  }

  /**
   * 通过peojectId获取指定项目的信息
   *
   * @param projectId
   * @return
   */
  @ApiOperation(value = "获取指定项目的信息", notes = "通过ID获取项目的信息")
  @GetMapping(value = "/{projectId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Project getProjectInfo(@PathVariable("projectId") String projectId) {

    return ofNullable(managerProjectService.getProjectById(projectId))
        .orElseThrow(() -> new NotFoundException(format("Project{id=%s} Not found", projectId)));
  }

  /**
   * 获取管理员创建的接口信息
   *
   * @param projectId
   * @return
   */
  @ApiOperation(value = "获取指定项目下的接口列表")
  @GetMapping(value = "/{projectId}/api", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<API> getApiList(@PathVariable("projectId") String projectId) {
    return apiService.getAPIListByProjectId(projectId);
  }

  @ApiOperation(value = "获取指定项目下的目录信息", notes = "获取的目录是全部的目录,可能包括空目录")
  @GetMapping(value = "/{projectId}/folder", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<Folder> getFoldersByProjectId(@PathVariable("projectId") String projectId) {
    return folderService.getFolderByProjectId(projectId);
  }

  @ApiOperation(
      value = "获知指定projectId的文档集合",
      notes = "**注意** 一个projectId可能对应多个doc")
  @GetMapping(value = "/{projectId}/doc", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<ProjectDoc> getProjectDocByProjectId(@PathVariable("projectId") String projectId) {
    return projectDocService.getProjectDocListByProjectId(projectId);
  }

  @ApiOperation(
      value = "获知指定projectId的环境值",
      notes = "**注意** 一个 projectId 可能对应多个环境值")
  @GetMapping(value = "/{projectId}/environment", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<Environment> getProjectEnvsByProjectId(@PathVariable("projectId") String projectId) {
    return environmentService.getProjectEnvByProjectId(projectId);
  }
}
