package com.zhuxun.dmc.apim.domain.project;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.repository.model.DcFolder;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Optional;

@Data
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Folder {
  String id;
  String name;
  String description;
  List<API> apiList;

  Project project;

  public static Folder of(DcFolder dcFolder) {
    return Optional.ofNullable(
            new Folder()
                .setId(dcFolder.getId())
                .setName(dcFolder.getFolderName())
                .setProject(new Project().setId(dcFolder.getProjectId()))
                .setDescription(dcFolder.getFolderDescription()))
        .orElse(null);
  }
}
