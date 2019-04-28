package com.zhuxun.dmc.apim.domain.api.apiimpl.graph;

import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.zhuxun.dmc.apim.domain.api.apiimpl.graph.errors.RelGraphImplException;
import com.zhuxun.dmc.sqlapi.Utils;

import java.util.Map;

public class JoinGraphDef extends GraphDef {
  @Override
  public Tuple<SQLQueryExpr, Map<String, Utils.Variable>> toGeneralQuery(String dbType) {
    throw new RelGraphImplException("JOIN is not supported yet");
  }
}
