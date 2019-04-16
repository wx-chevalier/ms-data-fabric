package com.zhuxun.dmc.sqlapi;


import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import io.swagger.v3.oas.models.media.Schema;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

public interface SQLAPIAnalyzer {
  SQLAPIAnalyzer setConnection(Connection connection);

  Connection getConnection();

  Boolean getIsQuery();

  Schema analyzeParameter(String sql, String dialect)
      throws SQLAPIException, SQLException;

  Schema analyzeResponse(String sql, String dialect, Map<String, Object> exampleValue)
      throws SQLAPIException, SQLException;
}
