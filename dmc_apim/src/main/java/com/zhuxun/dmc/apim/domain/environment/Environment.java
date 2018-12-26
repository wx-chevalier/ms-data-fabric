package com.zhuxun.dmc.apim.domain.environment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.zhuxun.dmc.apim.domain.common.Modifiable;
import com.zhuxun.dmc.apim.repository.model.DcEnvironment;
import com.zhuxun.dmc.apim.repository.model.DcEnvironmentWithBLOBs;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
@JsonInclude(Include.NON_NULL)
public class Environment extends Modifiable<Environment> {

  private String id;

  private String envValue;

  private String envName;

  private String remark;

  private String projectId;

  public static Environment of(DcEnvironmentWithBLOBs dcEnvironment) {
    return Optional.ofNullable(dcEnvironment)
        .map(environment -> new Environment()
            .setCreateDatetime(environment.getCreateDatetime())
            .setModifyDatetime(environment.getModifyDatetime())
            .setModifyUser(dcEnvironment.getModifyUserid())
            .setCreateUser(dcEnvironment.getCreateUserid())
            .setId(environment.getId())
            .setRemark(dcEnvironment.getRemark())
            .setProjectId(environment.getProjectId())
            .setEnvName(environment.getEnvName())
            .setEnvValue(environment.getEnvValue()))
        .orElse(null);
  }

  public static Environment of(DcEnvironment dcEnvironment) {
    return Optional.ofNullable(dcEnvironment)
        .map(environment -> new Environment()
            .setCreateDatetime(environment.getCreateDatetime())
            .setModifyDatetime(environment.getModifyDatetime())
            .setId(environment.getId())
            .setProjectId(environment.getProjectId())
            .setEnvName(environment.getEnvName()))
        .orElse(null);
  }
}
