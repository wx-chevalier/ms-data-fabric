package com.zhuxun.dc.apirunner.service.datasource.metadata;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Slf4j
public class MySQLMetadataRetreiver extends GeneralMetadataRetreiver implements MetadataRetreiver {
  @Override
  public Optional<String> getCurrentSchema(Connection connection) throws SQLException {
    try (ResultSet resultSet = connection.prepareStatement("SELECT DATABASE() FROM dual").executeQuery()) {
      if (resultSet.next()) {
        return Optional.ofNullable(resultSet.getString(1));
      }
      return Optional.empty();
    }
  }

  @Override
  public Set<String> getSchemaNames(Connection connection, String catalog) throws SQLException {
    try (ResultSet resultSet =
             connection.prepareStatement("SHOW DATABASES").executeQuery()) {
      ImmutableSet.Builder<String> builder = ImmutableSet.builder();
      while (resultSet.next()) {
        String name = resultSet.getString(1);
        switch (name) {
          case "information_schema":
          case "performance_schema":
          case "mysql":
          case "sys":
            break;
          default:
            builder.add(name);
        }
      }
      return builder.build();
    }
  }

  @Override
  public List<ColumnInfo> getColumns(Connection connection, String catalog, String schemaName, String tableName) throws SQLException {
    log.trace("getColumns {} {} {}", catalog, schemaName, tableName);
    DatabaseMetaData metadata = connection.getMetaData();
    String escape = metadata.getSearchStringEscape();
    try (ResultSet resultSet = metadata.getColumns(
        catalog,
        escapeNamePattern(schemaName, escape),
        escapeNamePattern(tableName, escape),
        "%")) {
      ImmutableList.Builder<MetadataRetreiver.ColumnInfo> builder = ImmutableList.builder();
      while (resultSet.next()) {
        String name = resultSet.getString("COLUMN_NAME");
        String type = resultSet.getString("TYPE_NAME");
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
