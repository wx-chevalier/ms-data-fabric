package com.zhuxun.dmc.apim.dto.project;

import com.zhuxun.dmc.apim.repository.model.DcFolder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

@Accessors(chain = true)
@Data
public class FolderCreation {
  String projectId;
  String name;
  String description;

  public DcFolder toEntity() {
    Date now = new Date();
    return new DcFolder()
        .withProjectId(projectId)
        .withFolderName(name)
        .withCreateDatetime(now)
        .withModifyDatetime(now)
        .withFolderDescription(description);
  }
}
