package com.zhuxun.dc.apirunner.service.datasource.metadata;

import lombok.Data;
import lombok.experimental.Accessors;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface MetadataRetreiver {
  Optional<String> getCurrentSchema(Connection connection) throws SQLException;

  Set<String> getCatalogs(Connection connection) throws SQLException;

  Set<String> getSchemaNames(Connection connection, String catalog) throws SQLException;

  Set<String> getTables(Connection connection, String catalog, String schemaName) throws SQLException;

  List<ColumnInfo> getColumns(Connection connection, String catalog, String schemaName, String tableName) throws SQLException;

  @Accessors(chain = true)
  @Data
  public static class ColumnInfo {
    String name;
    String type;
  }
}
