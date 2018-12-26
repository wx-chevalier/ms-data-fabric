package com.zhuxun.dc.apirunner.service.datasource.metadata;

import com.google.common.collect.ImmutableSet;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;

public class OracleMetadataRetreiver extends GeneralMetadataRetreiver implements MetadataRetreiver {
  @Override
  public Set<String> getCatalogs(Connection connection) throws SQLException {
    return super.getCatalogs(connection);
  }

  @Override
  public Set<String> getTables(Connection connection, String catalog, String schemaName) throws SQLException {
    DatabaseMetaData metadata = connection.getMetaData();
    if (metadata.storesUpperCaseIdentifiers()) {
      schemaName = schemaName.toUpperCase();
    }
    try (ResultSet resultSet = connection.getMetaData()
        .getTables(null, schemaName, null, new String[]{"TABLE", "SYNONYM"})) {
      ImmutableSet.Builder<String> builder = ImmutableSet.builder();
      while (resultSet.next()) {
        builder.add(resultSet.getString("TABLE_NAME"));
      }
      return builder.build();
    }
  }

  public Optional<String> getCurrentSchema(Connection connection) throws SQLException {
    try (ResultSet resultSet = connection.prepareStatement("SELECT SYS_CONTEXT('USERENV','CURRENT_SCHEMA') FROM DUAL").executeQuery()) {
      if (resultSet.next()) {
        return Optional.ofNullable(resultSet.getString(1));
      }
      return Optional.empty();
    }
  }
}
