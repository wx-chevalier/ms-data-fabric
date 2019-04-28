package com.zhuxun.dmc.sqlapi.datasource;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

@Accessors(chain = true)
@Data
public class ManagedDatasourceProperties {
  private DataSourceProperties presto = new DataSourceProperties();

  private String dsConfigDir;
}
