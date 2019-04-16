package com.zhuxun.dmc.apim.domain.api.apiimpl;

import io.swagger.annotations.ApiModel;
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
@ApiModel(parent = APIImplDef.class)
public class SQLImplDef extends APIImplDef {
  @ApiModelProperty(notes = "当前支持的有\n- mysql\n- sqlserver")
  String dialect;

  String sql;

  String datasourceName;

  String schemaName;

  Boolean isQuery;

  public Boolean getQuery() {
    return isQuery == null ? true : isQuery;
  }

  @Override
  public APIImplType getType() {
    return APIImplType.SQL;
  }
}
