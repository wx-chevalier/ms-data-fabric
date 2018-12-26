package com.zhuxun.dmc.sqlapi.impl;

import com.zhuxun.dmc.sqlapi.SQLAPIExecutor;
import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import com.zhuxun.dmc.sqlapi.utils.SQLUtils;
import io.swagger.v3.oas.models.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Accessors(chain = true)
@Slf4j
public class SQLAPIExecutorImpl implements SQLAPIExecutor {

  @Getter
  @Setter
  Connection connection;

  @Override
  public List<Map<String, Object>> executeQuery(
      String sql, String dialect, Map<String, Object> parameter, Schema parameterSchema)
      throws SQLAPIException, SQLException {
    VariableParser parsedSQL = new VariableParser(sql).parse();
    if (!SQLUtils.isQuery(parsedSQL.getVarReplacedSql(), dialect)) {
      throw new SQLAPIException("Not query: " + sql);
    }

    PreparedStatement stmt = SQLAPIUtils.prepareStmt(
        connection,
        parsedSQL,
        parameterSchema,
        parameter);
    ResultSet resultSet = stmt.executeQuery();
    ResultSetMetaData metaData = resultSet.getMetaData();
    int columnCount = metaData.getColumnCount();

    ArrayList<Map<String, Object>> result = new ArrayList<>();
    while (resultSet.next()) {
      Map<String, Object> item = new HashMap<>();
      for (int i = 1; i <= columnCount; i++) {
        item.put(
            metaData.getColumnLabel(i),
            resultSet.getObject(i));
      }
      result.add(item);
    }

    return result;
  }

  @Override
  public UpdateResult execute(
      String sql, String dialect, Map<String, Object> parameter, Schema parameterSchema)
      throws SQLException, SQLAPIException {

    PreparedStatement stmt = SQLAPIUtils.prepareStmt(
        connection,
        new VariableParser(sql).parse(),
        parameterSchema,
        parameter);
    int count = stmt.executeUpdate();

    return new UpdateResult().setAffected(count);
  }
}
