package com.zhuxun.dmc.apim.service.datasource.metadata;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class GeneralMetadataRetreiver implements MetadataRetreiver {
  public Set<String> getCatalogs(Connection connection) throws SQLException {
    return null;
  }

  public Optional<String> getCurrentSchema(Connection connection) throws SQLException {
    try {
      return Optional.ofNullable(connection.getSchema());
    } catch (SQLFeatureNotSupportedException e) {
      log.warn("获取当前 schema 失败", e);
      return Optional.empty();
    }
  }


  @Override
  public Set<String> getSchemaNames(Connection connection, String catalog) throws SQLException {
    try (ResultSet schemas = connection.getMetaData().getSchemas()) {
      ImmutableSet.Builder<String> builder = ImmutableSet.builder();
      while (schemas.next()) {
        String name = schemas.getString(1).toLowerCase();
        if (!"information_schema".equals(name)) {
          builder.add(name);
        }
      }
      ImmutableSet<String> names = builder.build();
      log.trace("getSchemaNames {} - {}", catalog, names);
      return names;
    }
  }

  @Override
  public Set<String> getTables(Connection connection, String catalog, String schemaName)
      throws SQLException {
    // 获取某一数据源下的全部数据库列表
    try (ResultSet resultSet =
             connection.prepareStatement(String.format("SHOW TABLES FROM %s", schemaName)).executeQuery()) {
      ImmutableSet.Builder<String> builder = ImmutableSet.builder();
      while (resultSet.next()) {
        builder.add(resultSet.getString(1));
      }
      ImmutableSet<String> names = builder.build();
      log.trace("getTables {} {} - {}", catalog, schemaName, names);
      return names;
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
        null)) {
      ImmutableList.Builder<MetadataRetreiver.ColumnInfo> builder = ImmutableList.builder();
      while (resultSet.next()) {
        String name = resultSet.getString("COLUMN_NAME");
        String type = resultSet.getString("TYPE_NAME");
        builder.add(new MetadataRetreiver.ColumnInfo()
            .setName(name)
            .setType(type));
      }
      ImmutableList<ColumnInfo> columns = builder.build();
      log.trace("getTables {} {} {} - {}", catalog, schemaName, tableName, columns);
      return columns;
    }
  }

  protected static String escapeNamePattern(String name, String escape) {
    if ((name == null) || (escape == null)) {
      return name;
    }
    checkArgument(!escape.equals("_"), "Escape string must not be '_'");
    checkArgument(!escape.equals("%"), "Escape string must not be '%'");
    name = name.replace(escape, escape + escape);
    name = name.replace("_", escape + "_");
    name = name.replace("%", escape + "%");
    return name;
  }

}
