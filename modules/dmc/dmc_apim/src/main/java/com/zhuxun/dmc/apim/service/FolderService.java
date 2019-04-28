package com.zhuxun.dmc.apim.service;

import com.zhuxun.dmc.apim.domain.project.Folder;
import com.zhuxun.dmc.apim.dto.project.FolderCreation;
import com.zhuxun.dmc.apim.dto.project.FolderModification;

import java.util.List;

public interface FolderService {
  List<Folder> getFolderByProjectId(String projectId);

  Folder getFolderById(String folderId);

  Folder createFolder(String createUserId, FolderCreation folderCreation);

  void deleteFolder(String folderId);

  Folder updateFolder(String modifyUserId, String folderId, FolderModification folderModification);
}
