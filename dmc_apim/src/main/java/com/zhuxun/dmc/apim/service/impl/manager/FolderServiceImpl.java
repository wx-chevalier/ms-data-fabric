package com.zhuxun.dmc.apim.service.impl.manager;


import com.google.common.base.Strings;
import com.zhuxun.dmc.apim.config.ApplicationConstants;
import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.project.Folder;
import com.zhuxun.dmc.apim.dto.project.FolderCreation;
import com.zhuxun.dmc.apim.dto.project.FolderModification;
import com.zhuxun.dmc.apim.repository.model.DcApiExample;
import com.zhuxun.dmc.apim.repository.model.DcFolderExample;
import com.zhuxun.dmc.apim.service.AbstractService;
import com.zhuxun.dmc.apim.service.FolderService;
import com.zhuxun.dmc.apim.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class FolderServiceImpl extends AbstractService implements FolderService {


  /**
   * 获取指定项目有下的目录信息
   */
  @Transactional(readOnly = true)
  public List<Folder> getFolderByProjectId(String projectId) {
    log.info("获取projectId = {} 的目录信息", projectId);

    DcFolderExample folderExample = new DcFolderExample();
    folderExample.createCriteria().andProjectIdEqualTo(projectId);
    return dcFolderMapper.selectByExample(folderExample).stream()
        .map(Folder::of)
        .collect(toList());
  }

  /**
   * 通过目录的id获取目录信息
   */
  @Transactional
  public Folder getFolderById(String folderId) {
    log.info("获取folderId = {} 的目录信息", folderId);

    return Optional.ofNullable(dcFolderMapper.selectByPrimaryKey(folderId))
        .map(Folder::of)
        .map(f -> {
          DcApiExample apiExample = new DcApiExample();
          apiExample.createCriteria().andFolderIdEqualTo(folderId);
          f.setApiList(dcApiMapper.selectByExample(apiExample).stream()
              .map(a -> new API().setId(a.getId()))
              .collect(toList()));
          return f;
        })
        .orElse(null);
  }

  /**
   * 新增目录
   */
  @Transactional
  public Folder createFolder(String createUserId, FolderCreation folderCreation) {
    checkArgument(!Strings.isNullOrEmpty(folderCreation.getName()), "新建分组失败，分组名为空");
    checkArgument(!Strings.isNullOrEmpty(folderCreation.getProjectId()), "新增分组失败,项目 ID 为空");
    log.info("新增目录 folder = {}", folderCreation);

    String folderId = UUIDUtils.getUUID();
    dcFolderMapper.insertSelective(folderCreation.toEntity()
        .withId(folderId)
        .withCreateUserid(createUserId)
        .withModifyUserid(createUserId)
        .withStatus(ApplicationConstants.STATUS_NORMAL));

    return getFolderById(folderId);
  }

  /**
   * 逻辑删除目录
   */
  @Transactional
  public void deleteFolder(String folderId) {
    apiMapper.clearFolder(folderId);
    dcFolderMapper.deleteByPrimaryKey(folderId);
  }

  /**
   * 更新目录信息，部分更新，非全局更新
   */
  @Transactional
  public Folder updateFolder(
      String modifyUserId,
      String folderId,
      FolderModification folderModification) {
    // TODO 校验此目录必须是此人创建方可逻辑更新
    dcFolderMapper.updateByPrimaryKeySelective(folderModification.toEntity()
        .withId(folderId)
        .withModifyUserid(modifyUserId)
        .withModifyDatetime(new Date()));
    return getFolderById(folderId);
  }

}
