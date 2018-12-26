package com.zhuxun.dmc.sqlapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import io.swagger.v3.oas.models.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface SQLAPIExecutor {
  Connection getConnection();

  List<Map<String, Object>> executeQuery(
      String sql,
      String dialect,
      Map<String, Object> parameter,
      Schema parameterSchema
  ) throws SQLAPIException, SQLException;

  UpdateResult execute(
      String sql,
      String dialect,
      Map<String, Object> parameter,
      Schema parameterSchema) throws SQLException, SQLAPIException;

  @Accessors(chain = true)
  class UpdateResult {
    @JsonProperty("affected")
    @Getter
    @Setter
    Integer affected;
  }
}
