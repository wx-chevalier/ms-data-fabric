package com.zhuxun.dmc.apim.dto.project;

import com.zhuxun.dmc.apim.repository.model.DcFolder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class FolderModification extends FolderCreation {
  Byte status;

  String remark;

  @Override
  public DcFolder toEntity() {
    return super.toEntity()
        .withStatus(status)
        .withRemark(remark);
  }
}
