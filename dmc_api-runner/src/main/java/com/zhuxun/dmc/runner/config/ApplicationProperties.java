package com.zhuxun.dmc.runner.config;

import com.zhuxun.dmc.sqlapi.datasource.ManagedDatasourceProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@EqualsAndHashCode(callSuper = true)
@Component
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Accessors(chain = true)
@Data
public class ApplicationProperties
    extends com.zhuxun.spring.config.properties.ApplicationProperties {

  private final ManagedDatasourceProperties managedDatasource = new ManagedDatasourceProperties();
}
