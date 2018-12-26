package com.zhuxun.dmc.apim.web.rest;

import com.zhuxun.dmc.apim.domain.project.ProjectDoc;
import com.zhuxun.dmc.apim.dto.project.ProjectDocTO;
import com.zhuxun.dmc.apim.security.jwt.TokenProvider;
import com.zhuxun.dmc.apim.service.ProjectDocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/projectdoc")
@Api(description = "项目文档管理相关接口")
public class ProjectDocResource extends AbstractResource {
  private ProjectDocService docService;

  @Autowired
  public ProjectDocResource(
      TokenProvider tokenProvider,
      ProjectDocService docService) {
    super(tokenProvider);
    this.docService = docService;
  }

  @ApiOperation(value = "获知指定docId的详情内容", notes = "获取单个文档信息")
  @GetMapping(value = "/{projectDocId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ProjectDoc getProjectDocById(@PathVariable("projectDocId") String projectDocId) {
    return docService.getProjectDocById(projectDocId);
  }


  @ApiOperation(value = "新增项目文档", notes = "必传参数:文档内容，所属项目ID")
  @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ProjectDoc addProjectDoc(@RequestBody ProjectDocTO projectDocTO) {
    return docService.createProjectDoc(
        currentUserOrThrow().getId(),
        projectDocTO);
  }

  @ApiOperation(value = "编辑项目文档", notes = "**必传参数** \n  - 文档ID \n - 更新内容字段")
  @PatchMapping(value = "/{projectDocId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ProjectDoc update(@PathVariable("projectDocId") String projectDocId, @RequestBody ProjectDocTO projectDocTO) {
    return docService.updateProjectDocById(
        currentUserOrThrow().getId(),
        projectDocId,
        projectDocTO);
  }

  @ApiOperation(value = "逻辑删除项目文档")
  @DeleteMapping(value = "/{projectDocId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public void deleteProjectDoc(@PathVariable("projectDocId") String projectDocId) {
    docService.logicDeleteProjectDoc(currentUserOrThrow().getId(), projectDocId);
  }
}
