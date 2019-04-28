package com.zhuxun.dc.apirunner.sqlapi;

import com.zhuxun.dc.apirunner.exception.SQLAPIException;
import io.swagger.v3.oas.models.media.Schema;

import java.sql.SQLException;

public interface SQLAPIAnalyzer {
  Schema analyzeParameter(String sql, String dialect)
      throws SQLAPIException, SQLException;

  Schema analyzeResponse(String sql, String dialect)
      throws SQLAPIException, SQLException;
}
