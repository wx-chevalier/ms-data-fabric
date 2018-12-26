package com.zhuxun.dmc.apim.service.datasource.metadata;

public class MetadataRetreiverBuilder {
  public static MetadataRetreiver of(String type) {
    switch (type) {
      case "presto":
        return new PrestoMetadataRetreiver();
      case "mysql":
        return new MySQLMetadataRetreiver();
      case "sqlserver":
        return new SqlServerMetadataRetreiver();
      case "oracle":
        return new OracleMetadataRetreiver();
      default:
        return new GeneralMetadataRetreiver();
    }
  }
}
