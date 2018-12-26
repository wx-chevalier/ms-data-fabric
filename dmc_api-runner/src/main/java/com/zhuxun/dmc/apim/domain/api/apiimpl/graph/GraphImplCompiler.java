package com.zhuxun.dmc.apim.domain.api.apiimpl.graph;

import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.zhuxun.dmc.apim.domain.api.apiimpl.SQLImplDef;
import com.zhuxun.dmc.apim.domain.api.apiimpl.graph.errors.RelGraphImplException;
import com.zhuxun.dmc.sqlapi.Utils;

import java.util.Map;
import java.util.Set;

public class GraphImplCompiler {
  public SQLImplDef compile(GraphDef graphDef) {
    if (graphDef instanceof TableGraphDef) {
      TableGraphDef def = (TableGraphDef) graphDef;
      if (def.getDatabase() == null && def.getDatabase().getDatasource() == null) {
        throw new RelGraphImplException("未指定数据源");
      }
      String dialect = def.getDatabase().getDatasource().getType();

      Tuple<SQLQueryExpr, Map<String, Utils.Variable>> sqlvars = def.toQuery(dialect);
      String processedSQL = replaceDecoratedVar(sqlvars.getX().toString(), sqlvars.getY());
      return new SQLImplDef()
          .setSql(processedSQL)
          .setDialect(dialect)
          .setSchemaName(def.getDatabase().getName())
          .setDatasourceName(def.getDatabase().getDatasource().getName());
    }
    throw new RelGraphImplException("Not supported yet");
  }

  String replaceDecoratedVar(String sql, Map<String, Utils.Variable> vars) {
    Set<String> varsInSql = Utils.findDecoratedVars(sql);
    String processedSQL = sql;
    for (String varName : varsInSql) {
      Utils.Variable var = vars.getOrDefault(varName, null);
      if (var == null) {
        throw new RelGraphImplException("变量不存在：" + varName);
      }
      processedSQL = processedSQL.replaceAll(Utils.decorateVar(varName), var.repr());
    }
    return processedSQL;
  }
}
