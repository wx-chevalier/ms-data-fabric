package com.zhuxun.dmc.apim.dto.api.apiimpl;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class SQLImpl extends APIImplTO {
  @ApiModelProperty(notes = "当前支持的有\n- mysql\n- sqlserver")
  String dialect;

  String sql;

  String datasourceName;

  @Override
  APIImplType getType() {
    return APIImplType.SQL;
  }
}
