package com.zhuxun.dmc.apim.dto.project;

import com.zhuxun.dmc.apim.dto.DocTO;
import com.zhuxun.dmc.apim.repository.model.DcProjectDoc;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ProjectDocTO extends DocTO {
  // 文档所属项目
  private String projectId;

  public DcProjectDoc toEntity() {
    return new DcProjectDoc()
        .withContent(content)
        .withProjectId(projectId)
        .withTitle(title)
        .withContentType(contentType);
  }
}
