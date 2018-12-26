package com.zhuxun.dc.apirunner.sqlapi;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zhuxun.dc.apirunner.exception.SQLAPIException;
import io.swagger.v3.oas.models.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;

public interface SQLAPiExecutor {
  Collection<Map<String, Object>> executeQuery(
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

  @Accessors(fluent = true, chain = true)
  class UpdateResult {
    @JsonProperty("affected")
    @Getter
    @Setter
    Integer affected;
  }
}
