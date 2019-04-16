package com.zhuxun.dc.apirunner.service.datasource.metadata;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class SqlServerMetadataRetreiver extends GeneralMetadataRetreiver implements MetadataRetreiver {
  @Override
  public Optional<String> getCurrentSchema(Connection connection) throws SQLException {
    try (ResultSet resultSet = connection.prepareStatement("SELECT DB_NAME()").executeQuery()) {
      if (resultSet.next()) {
        return Optional.ofNullable(resultSet.getString(1));
      }
      return Optional.empty();
    }
  }

  @Override
  public Set<String> getTables(Connection connection, String catalog, String schemaName) throws SQLException {
    try (ResultSet resultSet = connection.prepareStatement(
        String.format(
            "SELECT TABLE_NAME FROM information_schema.tables WHERE TABLE_TYPE='BASE TABLE' AND TABLE_CATALOG='%s'",
            schemaName)).executeQuery()) {
      ImmutableSet.Builder<String> builder = ImmutableSet.builder();
      while (resultSet.next()) {
        builder.add(resultSet.getString(1));
      }
      return builder.build();
    }
  }

  @Override
  public List<ColumnInfo> getColumns(Connection connection, String catalog, String schemaName, String tableName) throws SQLException {
    try (ResultSet resultSet = connection.prepareStatement(
        String.format(
            "SELECT COLUMN_NAME, DATA_TYPE FROM information_schema.columns WHERE TABLE_CATALOG='%s' AND TABLE_NAME='%s';",
            schemaName, tableName)).executeQuery()) {
      ImmutableList.Builder<MetadataRetreiver.ColumnInfo> builder = ImmutableList.builder();
      while (resultSet.next()) {
        String name = resultSet.getString("COLUMN_NAME");
        String type = resultSet.getString("DATA_TYPE");
        builder.add(new MetadataRetreiver.ColumnInfo()
            .setName(name)
            .setType(type));
      }
      ImmutableList<MetadataRetreiver.ColumnInfo> columns = builder.build();
      log.trace("getTables {} {} {} - {}", catalog, schemaName, tableName, columns);
      return columns;
    }
  }
}
