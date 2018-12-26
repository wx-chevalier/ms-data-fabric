package com.zhuxun.dmc.sqlapi.utils;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.facebook.presto.sql.parser.ParsingOptions;
import com.facebook.presto.sql.parser.SqlParser;
import com.facebook.presto.sql.tree.Query;
import com.facebook.presto.sql.tree.Statement;
import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;

import java.util.List;

public class SQLUtils {
  public static Boolean isQuery(String sql, String dialect) throws SQLAPIException {
    if ("presto".equals(dialect)) {
      return isPrestoQuery(sql);
    } else {
      List<SQLStatement> stmts = com.alibaba.druid.sql.SQLUtils.parseStatements(sql, dialect);
      if (stmts.size() == 0) {
        throw new SQLAPIException("Multiple statement not supported");
      }
      return stmts.get(0) instanceof SQLSelectStatement;
    }
  }

  public static Boolean isPrestoQuery(String sql) {
    Statement stmt = new SqlParser().createStatement(sql, new ParsingOptions());
    return stmt instanceof Query;
  }
}
