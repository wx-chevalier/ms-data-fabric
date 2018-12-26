package com.zhuxun.dc.apirunner.entity.dto;

import io.swagger.v3.oas.models.media.ObjectSchema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@EqualsAndHashCode(callSuper = true)
public class SqlExecution extends SqlAnalysis {
  ObjectSchema parameterSchema;
}
