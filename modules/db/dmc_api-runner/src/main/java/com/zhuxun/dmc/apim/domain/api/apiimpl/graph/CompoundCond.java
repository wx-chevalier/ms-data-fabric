package com.zhuxun.dmc.apim.domain.api.apiimpl.graph;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.zhuxun.dmc.apim.domain.api.apiimpl.graph.errors.RelGraphImplException;
import com.zhuxun.dmc.sqlapi.Utils;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

import static com.zhuxun.dmc.sqlapi.Utils.toSQLBinaryOperator;

@Accessors(chain = true)
@Data
public class CompoundCond extends Cond {
  String operator;
  Cond left;
  Cond right;

  @Override
  public Tuple<SQLExpr, Map<String, Utils.Variable>> toSQLExpr(String dbType) {
    SQLBinaryOperator op = toSQLBinaryOperator(getOperator());
    if (op != null) {
      if (!op.isLogical()) {
        throw new RelGraphImplException("非逻辑操作符: " + this);
      }
    } else {
      throw new RelGraphImplException("未知操作符: " + this);
    }

    Tuple<SQLExpr, Map<String, Utils.Variable>> leftTuple = left.toSQLExpr(dbType);
    Tuple<SQLExpr, Map<String, Utils.Variable>> rightTuple = right.toSQLExpr(dbType);
    Map<String, Utils.Variable> vars = new HashMap<>();
    vars = mergeVars(vars, leftTuple.getY());
    vars = mergeVars(vars, rightTuple.getY());
    return new Tuple<>(new SQLBinaryOpExpr(leftTuple.getX(), op, rightTuple.getX(), dbType), vars);
  }

  /** 将 varsFrom 中的变量合并到 varsDst 中 */
  public static Map<String, Utils.Variable> mergeVars(
      Map<String, Utils.Variable> varsDst, @Nullable Map<String, Utils.Variable> varsFrom) {
    if (varsFrom == null) {
      return varsDst;
    }
    for (Map.Entry<String, Utils.Variable> entry : varsFrom.entrySet()) {
      if (varsDst.containsKey(entry.getKey())) {
        Utils.Variable var = varsDst.get(entry.getKey());
        if (!var.isSame(entry.getValue())) {
          throw new RelGraphImplException("变量定义冲突：" + var + " vs. " + entry.getValue());
        } else {
          if (var.getDefaultValue() == null && entry.getValue().getDefaultValue() != null) {
            var.setDefaultValue(entry.getValue().getDefaultValue());
          }
        }
      } else {
        varsDst.put(entry.getKey(), entry.getValue());
      }
    }
    return varsDst;
  }
}
