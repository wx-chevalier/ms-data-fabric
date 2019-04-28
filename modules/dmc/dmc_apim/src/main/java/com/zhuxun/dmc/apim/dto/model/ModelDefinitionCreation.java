package com.zhuxun.dmc.apim.dto.model;

import com.zhuxun.dmc.apim.repository.model.DcApiModelDefinitionWithBLOBs;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@NoArgsConstructor
public class ModelDefinitionCreation {

  private String projectId;

  private String modelDefinition;

  private String modelDescribe;

  private String modelName;

  public DcApiModelDefinitionWithBLOBs toEntity(String id) {
    return (DcApiModelDefinitionWithBLOBs) new DcApiModelDefinitionWithBLOBs()
        .withModelDefinition(modelDefinition)
        .withModelDescribe(modelDescribe)
        .withId(id)
        .withProjectId(projectId)
        .withModelName(modelName);
  }
}
