package com.zhuxun.dmc.apim.web.rest;

import com.zhuxun.dmc.apim.domain.project.Folder;
import com.zhuxun.dmc.apim.dto.project.FolderCreation;
import com.zhuxun.dmc.apim.dto.project.FolderModification;
import com.zhuxun.dmc.apim.security.jwt.TokenProvider;
import com.zhuxun.dmc.apim.service.FolderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/folder")
@Api(description = "接口分组相关接口")
public class FolderResource extends AbstractResource {

  FolderService folderService;

  @Autowired
  public FolderResource(TokenProvider tokenProvider, FolderService folderService) {
    super(tokenProvider);
    this.folderService = folderService;
  }

  @ApiOperation(value = "获取目录详情")
  @GetMapping("/{folderId}")
  public Folder getFolder(@PathVariable("folderId") String folderId) {
    return folderService.getFolderById(folderId);
  }

  /**
   * 新增分组信息
   *
   * @return
   */
  @ApiOperation(value = "新增分组借口", notes = "必填参数:所属项目的id & 分组名称")
  @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Folder createFolder(@RequestBody FolderCreation folderCreation) {
    return folderService.createFolder(currentUserOrThrow().getId(), folderCreation);
  }

  /**
   * 修改分组信息
   *
   * @return
   */
  @ApiOperation(value = "修改分组信息接口", notes = "必填参数:目录ID,填写需要修改的信息，不需要修改的信息请勿提交")
  @PatchMapping(value = "/{folderId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Folder updateFolder(@PathVariable("folderId") String folderId, @RequestBody FolderModification folderModification) {
    return folderService.updateFolder(currentUserOrThrow().getId(), folderId, folderModification);
  }

  /**
   * 删除分组信息
   *
   * @param folderId 目录的ID
   * @return
   */
  @ApiOperation(value = "逻辑删除分组接口", notes = "必填参数:分组ID，此删除为逻辑删除，逻辑删除成功后status = 0 表示删除成功")
  @DeleteMapping(value = "/{folderId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public void delDcFolder(@PathVariable("folderId") String folderId) {
    folderService.deleteFolder(folderId);
  }
}
