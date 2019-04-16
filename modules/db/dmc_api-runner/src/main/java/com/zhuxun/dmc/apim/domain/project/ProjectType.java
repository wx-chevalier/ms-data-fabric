package com.zhuxun.dmc.apim.domain.project;

import com.zhuxun.dmc.apim.domain.common.Modifiable;
import com.zhuxun.dmc.apim.repository.model.DcProjectType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
public class ProjectType extends Modifiable<ProjectType> {
  String name;

  public static ProjectType of(DcProjectType t) {
    return new ProjectType().setId(t.getId()).setName(t.getTypeName());
  }
}
