package com.zhuxun.dmc.sqlapi.datasource;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.zhuxun.dmc.sqlapi.errors.ManagedDatasourceException;
import lombok.Data;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.*;

import static com.google.common.base.Strings.emptyToNull;

@Slf4j
public class ManagedDatasource {
  private ManagedDatasourceProperties managedDatasourceProperties;
  private DatasourceCache dsCache = new DatasourceCache();

  public ManagedDatasource(ManagedDatasourceProperties managedDatasourceProperties) {
    this.managedDatasourceProperties = managedDatasourceProperties;
  }

  public Connection getConnection(String dsName) throws ManagedDatasourceException {
    log.debug("获取对数据源 {} 的连接", dsName);

    Properties properties;
    if (dsName == null) {
      properties = prestoProperties();
    } else {
      DatasourceConfig dsConfig = getDsConfig(dsName);
      properties = dsConfig.toProperties();
    }


    try {
      Optional<DataSource> ds = dsCache.get(dsName);
      if (ds.isPresent()) {
        return ds.get().getConnection();
      } else {
        return dsCache.put(
            dsName,
            DruidDataSourceFactory.createDataSource(properties)).getConnection();
      }
    } catch (Exception e) {
      throw new ManagedDatasourceException("数据源创建出错: " + dsName, e);
    }
  }

  public DatasourceConfig getDsConfig(String dsName) throws ManagedDatasourceException {
    return DatasourceConfig.fromPropertyFile(getDsConfigFile(dsName));
  }

  public File getDsConfigFile(String dsName) throws ManagedDatasourceException {
    File configFile = new File(
        managedDatasourceProperties.getDsConfigDir(),
        dsName + ".properties");
    if (!configFile.exists()) {
      throw new ManagedDatasourceException("数据源配置不存在: " + dsName);
    }
    return configFile;
  }

  private Properties prestoProperties() {
    Properties prop = new Properties();

    DataSourceProperties presto = managedDatasourceProperties.getPresto();
    prop.setProperty(DruidDataSourceFactory.PROP_USERNAME, presto.getUsername());
    prop.setProperty(DruidDataSourceFactory.PROP_URL, presto.getUrl());
    if (emptyToNull(presto.getPassword()) != null) {
      prop.setProperty(DruidDataSourceFactory.PROP_PASSWORD, presto.getDataPassword());
    }

    return prop;
  }

  public class DatasourceCache {
    Map<String, Integer> dsNameToHash = new HashMap<>();
    Map<Integer, DataSource> dsHashToDs = new HashMap<>();

    public Optional<DataSource> get(String dsName) throws ManagedDatasourceException {
      Integer dsHash = getDsHash(dsName);
      Integer oldDsHash = dsNameToHash.getOrDefault(dsName, null);
      if (oldDsHash != null) {
        if (oldDsHash.equals(dsHash)) {
          return Optional.of(dsHashToDs.get(oldDsHash));
        } else {
          dsHashToDs.remove(oldDsHash);
          dsNameToHash.remove(dsName);
          return Optional.empty();
        }
      } else {
        return Optional.empty();
      }
    }

    public DataSource put(String dsName, DataSource dataSource) throws ManagedDatasourceException {
      Integer dsHash = getDsHash(dsName);
      Integer oldHash = dsNameToHash.getOrDefault(dsName, null);
      if (oldHash != null) {
        if (dsHashToDs.containsKey(oldHash)) {
          dsHashToDs.remove(oldHash);
        }
      }
      dsNameToHash.put(dsName, dsHash);
      dsHashToDs.put(dsHash, dataSource);
      return dataSource;
    }

    private Integer getDsHash(String dsName) throws ManagedDatasourceException {
      if (dsName == null) {
        DataSourceProperties presto = managedDatasourceProperties.getPresto();
        return Objects.hash(presto.getUsername(), presto.getUrl(), presto.getPassword());
      } else {
        DatasourceConfig dsConfig = getDsConfig(dsName);
        return Objects.hash(
            dsConfig.getType(),
            dsConfig.getUser(),
            dsConfig.getUrl(),
            dsConfig.getPassword());
      }
    }
  }

  @Accessors(chain = true)
  @Data
  public static class DatasourceConfig {
    String type;

    String url;

    String user;

    String password;

    public Properties toProperties() {
      Properties prop = new Properties();
      prop.setProperty(DruidDataSourceFactory.PROP_USERNAME, user);
      prop.setProperty(DruidDataSourceFactory.PROP_URL, url);
      if (emptyToNull(password) != null) {
        prop.setProperty(DruidDataSourceFactory.PROP_PASSWORD, password);
      }

      if ("oracle".equals(type)) {
        // https://github.com/alibaba/druid/issues/2377
        prop.setProperty(DruidDataSourceFactory.PROP_VALIDATIONQUERY, "SELECT 1 from dual");
      } else {
        prop.setProperty(DruidDataSourceFactory.PROP_VALIDATIONQUERY, "SELECT 1");
      }
      prop.setProperty(DruidDataSourceFactory.PROP_TESTWHILEIDLE, "true");
      prop.setProperty(DruidDataSourceFactory.PROP_TESTONBORROW, "true");
      prop.setProperty(DruidDataSourceFactory.PROP_TIMEBETWEENEVICTIONRUNSMILLIS, "600000");
      prop.setProperty(DruidDataSourceFactory.PROP_MAXWAIT, "10000");
      prop.setProperty(DruidDataSourceFactory.PROP_MAXACTIVE, "50");

      return prop;
    }

    public static DatasourceConfig fromPropertyFile(File f) throws ManagedDatasourceException {
      FileInputStream ins;
      Properties properties = new Properties();
      try {
        ins = new FileInputStream(f);
        properties.load(ins);
      } catch (IOException e) {
        throw new ManagedDatasourceException("数据源配置加载失败: " + f, e);
      }

      return new DatasourceConfig()
          .setType(properties.getProperty("connector.name"))
          .setUrl(properties.getProperty("connection-url"))
          .setUser(properties.getProperty("connection-user"))
          .setPassword(properties.getProperty("connection-password"));
    }
  }
}
