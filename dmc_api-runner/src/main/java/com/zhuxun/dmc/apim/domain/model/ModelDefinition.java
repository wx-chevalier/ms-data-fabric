package com.zhuxun.dmc.apim.domain.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.zhuxun.dmc.apim.repository.model.DcApiModelDefinitionWithBLOBs;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Optional;

@Data
@Accessors(chain = true)
@NoArgsConstructor
@JsonInclude
public class ModelDefinition {

  private String id;

  private String projectId;

  private String modelDefinition;

  private String modelDescribe;

  private String modelName;

  public static ModelDefinition of(DcApiModelDefinitionWithBLOBs dcApiModelDefinition) {
    return Optional.ofNullable(dcApiModelDefinition)
        .map(
            apiMode ->
                new ModelDefinition()
                    .setId(apiMode.getId())
                    .setModelDefinition(apiMode.getModelDefinition())
                    .setModelDescribe(apiMode.getModelDescribe())
                    .setProjectId(apiMode.getProjectId())
                    .setModelName(apiMode.getModelName()))
        .orElse(null);
  }
}
