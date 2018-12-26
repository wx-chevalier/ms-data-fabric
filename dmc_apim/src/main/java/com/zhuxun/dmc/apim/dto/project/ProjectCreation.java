package com.zhuxun.dmc.apim.dto.project;

import com.zhuxun.dmc.apim.repository.model.DcProjectWithBLOBs;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.function.Function;

@Accessors(chain = true)
@Data
public class ProjectCreation {
  String name;
  String type;
  String version;
  String description;

  public DcProjectWithBLOBs toEntity(Function<String, String> getTypeIdByName) {
    return (DcProjectWithBLOBs) new DcProjectWithBLOBs()
        .withProjectDescription(description)
        .withProjectName(name)
        .withProjectTypeId(getTypeIdByName.apply(type))
        .withProjectVersion(version);
  }
}
