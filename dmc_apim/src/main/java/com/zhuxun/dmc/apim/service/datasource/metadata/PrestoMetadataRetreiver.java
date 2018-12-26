package com.zhuxun.dmc.apim.service.datasource.metadata;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;

public class PrestoMetadataRetreiver extends GeneralMetadataRetreiver implements MetadataRetreiver {
  final static String SHOW_CATALOGS = "SHOW CATALOGS";
  final static String SHOW_SCHEMAS = "SHOW SCHEMAS FROM \"%s\"";
  final static String SHOW_TABLES = "SHOW TABLES FROM \"%s\".\"%s\"";
  final static String DESC_TABLE = "DESC \"%s\".\"%s\".\"%s\"";

  @Override
  public Set<String> getCatalogs(Connection connection) throws SQLException {
    return queryNames(connection, SHOW_CATALOGS);
  }

  @Override
  public Set<String> getSchemaNames(Connection connection, String catalogName) throws SQLException {
    return queryNames(connection, String.format(SHOW_SCHEMAS, catalogName));
  }

  @Override
  public Set<String> getTables(Connection connection, String catalogName, String schemaName) throws SQLException {
    return queryNames(connection, String.format(SHOW_TABLES, catalogName, schemaName));
  }

  @Override
  public List<ColumnInfo> getColumns(Connection connection, String catalogName, String schemaName, String tableName) throws SQLException {
    try (ResultSet resultSet = connection
        .prepareStatement(String.format(
            DESC_TABLE, catalogName, schemaName, tableName))
        .executeQuery()) {
      ImmutableList.Builder<ColumnInfo> builder = ImmutableList.builder();
      while (resultSet.next()) {
        builder.add(new ColumnInfo()
            .setName(resultSet.getString(1))
            .setType(resultSet.getString(2)));
      }
      return builder.build();
    }
  }

  private Set<String> queryNames(Connection connection, String sql) throws SQLException {
    try (ResultSet resultSet =
             connection.prepareStatement(sql).executeQuery()) {
      ImmutableSet.Builder<String> names = ImmutableSet.builder();
      while (resultSet.next()) {
        names.add(resultSet.getString(1));
      }
      return names.build();
    }
  }
}
