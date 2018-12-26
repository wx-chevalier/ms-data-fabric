package com.zhuxun.dmc.apim.dto.environment;

import com.zhuxun.dmc.apim.repository.model.DcEnvironmentWithBLOBs;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 环境传输对象
 */
@Accessors(chain = true)
@Data
public class EnvironmentDTO {

  private String envName;

  private String envValue;

  private String remark;

  private String projectId;

  public DcEnvironmentWithBLOBs toEntity() {
    return (DcEnvironmentWithBLOBs) new DcEnvironmentWithBLOBs()
        .withEnvValue(envValue)
        .withRemark(remark)
        .withEnvName(envName)
        .withProjectId(projectId);
  }
}
