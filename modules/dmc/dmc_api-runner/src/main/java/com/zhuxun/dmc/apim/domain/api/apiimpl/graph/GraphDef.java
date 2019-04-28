package com.zhuxun.dmc.apim.domain.api.apiimpl.graph;

import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
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
  @JsonSubTypes.Type(value = TableGraphDef.class, name = "TABLE"),
  @JsonSubTypes.Type(value = JoinGraphDef.class, name = "JOIN")
})
public abstract class GraphDef {
  public abstract Tuple<SQLQueryExpr, Map<String, Utils.Variable>> toGeneralQuery(String dbType);
}
