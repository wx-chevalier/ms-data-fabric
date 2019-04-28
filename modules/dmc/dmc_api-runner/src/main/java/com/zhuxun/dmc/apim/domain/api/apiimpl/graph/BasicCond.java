package com.zhuxun.dmc.apim.domain.api.apiimpl.graph;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.zhuxun.dmc.apim.domain.api.apiimpl.graph.errors.RelGraphImplException;
import com.zhuxun.dmc.sqlapi.Utils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.Map;

import static com.zhuxun.dmc.sqlapi.Utils.*;

@Accessors(chain = true)
@Data
public class BasicCond extends Cond {
  TableGraphDef.Column left;
  String operator;
  String right;

  @Override
  public Tuple<SQLExpr, Map<String, Variable>> toSQLExpr(String dbType) {
    Utils.Variable var = parseVar(getRight());
    SQLBinaryOperator op = toSQLBinaryOperator(getOperator());
    if (op != null) {
      if (!op.isRelational()) {
        throw new RelGraphImplException("非关系操作符: " + this);
      }
    } else {
      throw new RelGraphImplException("未知操作符: " + this);
    }
    if (var == null) {
      try {
        SQLExpr expr =
            SQLParserUtils.createExprParser(getRight(), dbType == "presto" ? "mysql" : dbType)
                .expr();
        if (!(expr instanceof SQLValuableExpr)) {
          throw new RuntimeException();
        }
        return new Tuple<>(new SQLBinaryOpExpr(getLeft().toExpr(dbType), op, expr), null);
      } catch (Exception e) {
        throw new RelGraphImplException("值错误: " + this);
      }
    } else {
      Map<String, Variable> vars = new HashMap<>();
      vars.put(var.getName(), var);
      return new Tuple<>(
          new SQLBinaryOpExpr(
              getLeft().toExpr(dbType),
              op,
              new SQLIdentifierExpr(decorateVar(var.getName())),
              dbType == "presto" ? "mysql" : dbType),
          vars);
    }
  }
}
