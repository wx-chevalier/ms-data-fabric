package com.zhuxun.dmc.apim.domain.api.apiimpl.graph;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.zhuxun.dmc.sqlapi.Utils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Map;

@Accessors(chain = true)
@Data
@NoArgsConstructor
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = BasicCond.class, name = "basic"),
  @JsonSubTypes.Type(value = CompoundCond.class, name = "compound")
})
public abstract class Cond {
  public abstract Tuple<SQLExpr, Map<String, Utils.Variable>> toSQLExpr(String dbType);
}
