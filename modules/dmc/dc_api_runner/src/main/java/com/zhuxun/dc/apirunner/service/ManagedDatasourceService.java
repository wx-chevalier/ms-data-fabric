package com.zhuxun.dc.apirunner.service;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.alibaba.druid.util.LRUCache;
import com.google.common.base.Strings;
import com.zhuxun.dc.apirunner.exception.ManagedDatasourceException;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static com.google.common.base.Preconditions.checkState;
import static com.google.common.base.Strings.emptyToNull;
import static com.google.common.base.Strings.isNullOrEmpty;

@Service
@Slf4j
public class ManagedDatasourceService extends BaseService {
  public static final String CONNECTION_NAME = "connector.name";
  private static final String CONNECTION_URL = "connection-url";
  private static final String CONNECTION_USER = "connection-user";
  private static final String CONNECTION_PASSWORD = "connection-password";

  Map<String, String> cachedDatasourceConfigHash = new HashMap<>();
  DataSource prestoDatasource = null;
  Map<String, DataSource> datasourceCache = new LRUCache<>(10);

  public Connection getConnection(String dsName) throws ManagedDatasourceException {
    log.debug("获取对数据源 {} 的连接", dsName);
    Properties properties = getDatasourceProperties(dsName);
    DataSource dataSource = null;

    // 尝试使用缓存中的数据源
    if (dsName == null && prestoDatasource != null) {
      dataSource = prestoDatasource;
    } else {
      if (datasourceCache.containsKey(dsName)) {
        dataSource = datasourceCache.get(dsName);
      }
    }

    // 缓存不存在
    if (dataSource == null) {
      try {
        dataSource = DruidDataSourceFactory.createDataSource(properties);

        if (dsName == null) {
          prestoDatasource = dataSource;
        } else {
          datasourceCache.put(dsName, dataSource);
        }
      } catch (Exception e) {
        log.error("创建数据源连接异常", e);
        throw new ManagedDatasourceException("创建数据源连接出错", e);
      }
    }

    try {
      return dataSource.getConnection();
    } catch (SQLException e) {
      log.error("数据源连接异常" + dsName, e);
      throw new ManagedDatasourceException("数据源连接出错: " + dsName, e);
    }
  }

  public File getDsConfigFile(String dsName) throws ManagedDatasourceException {
    File file = new File(configDir, dsName + ".properties");
    if (!file.exists()) {
      throw new ManagedDatasourceException("数据源配置不存在" + dsName);
    }
    if (!file.canRead()) {
      throw new ManagedDatasourceException("数据源配置不可读" + dsName);
    }
    return file;
  }

  /**
   * 获取数据源配置, 如果配置发生改变，清理连接池缓存
   *
   * @param dsName
   * @return
   * @throws ManagedDatasourceException
   */
  public Properties getDatasourceProperties(String dsName) throws ManagedDatasourceException {
    checkCache(dsName);

    if (Strings.isNullOrEmpty(dsName)) {
      return defaultDataSourceProperties();
    }

    FileInputStream ins;
    Properties properties = new Properties();
    File file = getDsConfigFile(dsName);
    try {
      ins = new FileInputStream(file);
      properties.load(ins);
    } catch (FileNotFoundException e) {
      throw new ManagedDatasourceException("数据源配置丢失: " + dsName);
    } catch (IOException e) {
      throw new ManagedDatasourceException("数据源配置丢失：" + file);
    }

    String name = properties.getProperty(CONNECTION_NAME);
    String url = properties.getProperty(CONNECTION_URL);
    String user = properties.getProperty(CONNECTION_USER);
    String password = properties.getProperty(CONNECTION_PASSWORD);

    try {
      checkState(!isNullOrEmpty(name), "连接类型不存在");
      checkState(!isNullOrEmpty(url), "连接 URL 不存在");
      checkState(!isNullOrEmpty(user), "用户名不存在");
    } catch (IllegalStateException e) {
      throw new ManagedDatasourceException("配置文件格式错误");
    }

    Properties dsProperties = new Properties();
    dsProperties.setProperty(DruidDataSourceFactory.PROP_USERNAME, user);
    dsProperties.setProperty(DruidDataSourceFactory.PROP_URL, url);
    if ("oracle".equals(name)) {
      // https://github.com/alibaba/druid/issues/2377
      dsProperties.setProperty(DruidDataSourceFactory.PROP_VALIDATIONQUERY, "SELECT 1 from dual");
    } else {
      dsProperties.setProperty(DruidDataSourceFactory.PROP_VALIDATIONQUERY, "SELECT 1");
    }
    dsProperties.setProperty(DruidDataSourceFactory.PROP_TESTWHILEIDLE, "true");
    dsProperties.setProperty(DruidDataSourceFactory.PROP_TESTONBORROW, "true");
    dsProperties.setProperty(DruidDataSourceFactory.PROP_TIMEBETWEENEVICTIONRUNSMILLIS, "600000");
    dsProperties.setProperty(DruidDataSourceFactory.PROP_MAXWAIT, "10000");
    dsProperties.setProperty(DruidDataSourceFactory.PROP_MAXACTIVE, "50");
    dsProperties.setProperty(CONNECTION_NAME, name);
    if (emptyToNull(password) != null) {
      dsProperties.setProperty(DruidDataSourceFactory.PROP_PASSWORD, password);
    }

    log.debug("数据源配置[{}]: {}", dsName, dsProperties);
    return dsProperties;
  }

  /**
   * 获取默认数据源配置
   *
   * @return
   */
  private Properties defaultDataSourceProperties() {
    Properties dsProperties = new Properties();
    dsProperties.setProperty(DruidDataSourceFactory.PROP_USERNAME, defaultUsername);
    dsProperties.setProperty(DruidDataSourceFactory.PROP_URL, defaultUrl);
    if (emptyToNull(defaultPassword) != null) {
      dsProperties.setProperty(DruidDataSourceFactory.PROP_PASSWORD, defaultPassword);
    }
    return dsProperties;
  }

  private void checkCache(String dsName) throws ManagedDatasourceException {
    if (!cachedDatasourceConfigHash.containsKey(dsName)) {
      return;
    }
    try {
      String configHash = Files
          .asByteSource(getDsConfigFile(dsName))
          .hash(Hashing.md5()).toString();
      if (!configHash.equals(cachedDatasourceConfigHash.get(dsName))) {
        datasourceCache.remove(dsName);
      }
    } catch (IOException e) {
      throw new ManagedDatasourceException("数据源配置读取错误", e);
    }
  }

}
