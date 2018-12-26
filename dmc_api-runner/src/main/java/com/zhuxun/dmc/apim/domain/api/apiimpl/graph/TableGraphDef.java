package com.zhuxun.dmc.apim.domain.api.apiimpl.graph;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLPropertyExpr;
import com.alibaba.druid.sql.ast.expr.SQLQueryExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectItem;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.zhuxun.dmc.apim.domain.api.apiimpl.graph.errors.RelGraphImplException;
import com.zhuxun.dmc.sqlapi.Utils;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.zhuxun.dmc.sqlapi.Utils.prestoQuoteIdentifier;

@Accessors(chain = true)
@Data
public class TableGraphDef extends GraphDef {
  String name;
  List<Column> columns;
  List<Cond> conditions;
  Database database;

  @Accessors(chain = true)
  @Data
  public static class Column {
    String name;
    String alias;

    public SQLIdentifierExpr toExpr(String dbType) {
      return new SQLIdentifierExpr(
          dbType == "presto" ? prestoQuoteIdentifier(getName()) : getName());
    }
  }

  @Accessors(chain = true)
  @Data
  public static class Database {
    String name;
    Datasource datasource;
  }

  @Accessors(chain = true)
  @Data
  public static class Datasource {
    String name;
    String type;
  }

  public Tuple<SQLQueryExpr, Map<String, Utils.Variable>> toQuery(String dbType) {
    switch (dbType) {
      case "mysql":
      case "sqlserver":
        return toGeneralQuery(dbType);
      case "presto":
        return toPresotQuery();
    }
    return null;
  }

  public Tuple<SQLQueryExpr, Map<String, Utils.Variable>> toGeneralQuery(String dbType) {
    if (getDatabase() == null || getDatabase().getDatasource() == null) {
      throw new RelGraphImplException("数据源未定");
    }
    SQLSelectQueryBlock queryBlock = new SQLSelectQueryBlock();
    SQLExprTableSource tableSource = new SQLExprTableSource();
    tableSource.setExpr(getName());
    tableSource.setSchema(getDatabase().getName());

    queryBlock.setFrom(tableSource);

    for (Column column : getColumns()) {
      SQLSelectItem item = new SQLSelectItem();
      if (getDatabase() != null) {
        item.setExpr(
            new SQLPropertyExpr(
                new SQLPropertyExpr(getDatabase().getName(), getName()), column.getName()));
      } else {
        item.setExpr(new SQLPropertyExpr(getName(), column.getName()));
      }
      if (column.getAlias() != null) {
        item.setAlias(column.getAlias());
      }
      queryBlock.getSelectList().add(item);
    }

    SQLExpr whereExpr = null;
    Map<String, Utils.Variable> vars = new HashMap<>();
    for (Cond cond : getConditions()) {
      Tuple<SQLExpr, Map<String, Utils.Variable>> exprVars = cond.toSQLExpr(dbType);
      CompoundCond.mergeVars(vars, exprVars.getY());
      if (whereExpr == null) {
        whereExpr = exprVars.getX();
      } else {
        whereExpr =
            new SQLBinaryOpExpr(whereExpr, Utils.toSQLBinaryOperator("AND"), exprVars.getX());
      }
    }
    queryBlock.setWhere(whereExpr);

    return new Tuple<>(new SQLQueryExpr(new SQLSelect(queryBlock)), vars);
  }

  /**
   * 暂时 presto 查询和 {@link #toGeneralQuery(String)} 几乎一致，除了表的处理
   *
   * @return
   */
  public Tuple<SQLQueryExpr, Map<String, Utils.Variable>> toPresotQuery() {
    if (getDatabase() == null || getDatabase().getDatasource() == null) {
      throw new RelGraphImplException("数据源未定");
    }
    SQLSelectQueryBlock queryBlock = new SQLSelectQueryBlock();
    SQLExprTableSource tableSource = new SQLExprTableSource();
    String dsName = getDatabase().getDatasource().getName();
    String dbName = getDatabase().getName();
    tableSource.setExpr(
        new SQLPropertyExpr(
            new SQLPropertyExpr(prestoQuoteIdentifier(dsName), prestoQuoteIdentifier(dbName)),
            prestoQuoteIdentifier(getName())));

    queryBlock.setFrom(tableSource);

    for (Column column : getColumns()) {
      SQLSelectItem item = new SQLSelectItem();
      if (getDatabase() != null) {
        item.setExpr(
            new SQLPropertyExpr(
                new SQLPropertyExpr(getDatabase().getName(), getName()), column.getName()));
      } else {
        item.setExpr(new SQLPropertyExpr(getName(), column.getName()));
      }
      if (column.getAlias() != null) {
        item.setAlias(column.getAlias());
      }
      queryBlock.getSelectList().add(item);
    }

    SQLExpr whereExpr = null;
    Map<String, Utils.Variable> vars = new HashMap<>();
    for (Cond cond : getConditions()) {
      Tuple<SQLExpr, Map<String, Utils.Variable>> exprVars = cond.toSQLExpr("presto");
      CompoundCond.mergeVars(vars, exprVars.getY());
      if (whereExpr == null) {
        whereExpr = exprVars.getX();
      } else {
        whereExpr =
            new SQLBinaryOpExpr(whereExpr, Utils.toSQLBinaryOperator("AND"), exprVars.getX());
      }
    }
    queryBlock.setWhere(whereExpr);

    return new Tuple<>(new SQLQueryExpr(new SQLSelect(queryBlock)), vars);
  }
}
