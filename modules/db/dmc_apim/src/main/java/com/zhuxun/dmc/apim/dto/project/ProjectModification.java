package com.zhuxun.dmc.apim.dto.project;

import com.zhuxun.dmc.apim.repository.model.DcProjectWithBLOBs;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.function.Function;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class ProjectModification extends ProjectCreation {
  Byte status;

  String remark;

  public DcProjectWithBLOBs toEntity(Function<String, String> getTypeIdByName) {
    return (DcProjectWithBLOBs) super.toEntity(getTypeIdByName)
        .withRemark(remark)
        .withModifyDatetime(new Date())
        .withStatus(status);
  }
}
