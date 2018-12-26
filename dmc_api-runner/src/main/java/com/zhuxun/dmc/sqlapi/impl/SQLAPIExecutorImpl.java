package com.zhuxun.dmc.sqlapi.impl;

import com.zhuxun.dmc.sqlapi.SQLAPIExecutor;
import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import com.zhuxun.dmc.sqlapi.utils.SQLUtils;
import io.swagger.v3.oas.models.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Accessors(chain = true)
@Slf4j
public class SQLAPIExecutorImpl implements SQLAPIExecutor {

  @Getter @Setter DataSource dataSource;

  @Override
  public List<Map<String, Object>> executeQuery(
      String sql, String dialect, Map<String, Object> parameter, Schema parameterSchema)
      throws SQLAPIException, SQLException {
    if (parameterSchema == null) {
      throw new SQLAPIException("参数 Schema 未指定");
    }
    VariableParser parsedSQL = new VariableParser(sql).parse();
    if (!SQLUtils.isQuery(parsedSQL.getVarReplacedSql(), dialect)) {
      throw new SQLAPIException("非查询接口: " + sql);
    }

    try (Connection connection = getDataSource().getConnection()) {
      PreparedStatement stmt =
          SQLAPIUtils.prepareStmt(
              connection,
              parsedSQL,
              parameterSchema,
              parameter,
              SQLAPIUtils.DefaultValueType.DEFAULT);
      ResultSet resultSet = stmt.executeQuery();
      ResultSetMetaData metaData = resultSet.getMetaData();
      int columnCount = metaData.getColumnCount();

      ArrayList<Map<String, Object>> result = new ArrayList<>();
      while (resultSet.next()) {
        Map<String, Object> item = new HashMap<>();
        for (int i = 1; i <= columnCount; i++) {
          item.put(metaData.getColumnLabel(i), resultSet.getObject(i));
        }
        result.add(item);
      }

      return result;
    }
  }

  @Override
  public UpdateResult execute(
      String sql, String dialect, Map<String, Object> parameter, Schema parameterSchema)
      throws SQLException, SQLAPIException {
    if (parameterSchema == null) {
      throw new SQLAPIException("参数 Schema 未指定");
    }
    PreparedStatement stmt;
    try (Connection connection = getDataSource().getConnection()) {
      stmt =
          SQLAPIUtils.prepareStmt(
              connection,
              new VariableParser(sql).parse(),
              parameterSchema,
              parameter,
              SQLAPIUtils.DefaultValueType.DEFAULT);
    }
    int count = stmt.executeUpdate();

    return new UpdateResult().setAffected(count);
  }
}
