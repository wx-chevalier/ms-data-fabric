package com.zhuxun.dmc.apim.service.datasource;

import com.google.common.io.Files;
import com.zhuxun.dmc.apim.config.ApplicationConstants;
import com.zhuxun.dmc.apim.config.properties.ApplicationProperties;
import com.zhuxun.dmc.apim.domain.datasource.Datasource;
import com.zhuxun.dmc.apim.repository.DataSourceMapper;
import com.zhuxun.dmc.apim.repository.model.*;
import com.zhuxun.dmc.apim.service.AbstractService;
import com.zhuxun.dmc.apim.service.datasource.metadata.MetadataRetreiver;
import com.zhuxun.dmc.apim.service.datasource.metadata.MetadataRetreiverBuilder;
import com.zhuxun.dmc.apim.utils.UUIDUtils;
import com.zhuxun.dmc.sqlapi.datasource.ManagedDatasource;
import com.zhuxun.dmc.sqlapi.errors.ManagedDatasourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * 数据源初始化服务
 */
@Service
@Slf4j
public class DatasourceInitService extends AbstractService {

  private ManagedDatasource managedDatasource;

  private DataSourceMapper dataSourceMapper;

  private String configDir;

  @Autowired
  public DatasourceInitService(ApplicationProperties applicationProperties,
                               DataSourceMapper dataSourceMapper,
                               ManagedDatasource managedDatasource) {
    this.managedDatasource = managedDatasource;
    this.dataSourceMapper = dataSourceMapper;
    this.configDir = applicationProperties.getManagedDatasource().getDsConfigDir();
  }

  @PostConstruct
  public void automaticallyInitDatasources() {
    try {
      this.initAllDatasource();
    } catch (Exception e) {
      log.error("数据源初始化错误: " + this.configDir, e);
    }
  }

  @Transactional
  public void removeAllDatasource() {
    List<DcDatasource> dcDataSources = dcDatasourceMapper.selectByExample(new DcDatasourceExample());
    for (DcDatasource dataSource : dcDataSources) {
      removeDatasource(dataSource.getDataSourceName());
    }
  }

  /**
   * 根据配置文件的名称删除所有的数据库和表以及字段
   */
  @Transactional
  public void removeDatasource(String dsName) {
    log.info("移除数据源 {}", dsName);
    Datasource ds = dataSourceMapper.selectDataSourceByName(dsName);
    if (ds != null && ds.getId() != null) {
      dataSourceMapper.deleteColumnByDatasourceId(ds.getId());
      dataSourceMapper.deleteTableByDatasourceId(ds.getId());
      dataSourceMapper.deleteSchemaByDatasourceId(ds.getId());
      dataSourceMapper.deleteDataSourceByName(dsName);
    }
  }

  @Transactional
  public void initAllDatasource() {
    removeAllDatasource();

    if (configDir == null) {
      log.warn("数据源配置目录未配置");
      return;
    }

    File configDirFile = new File(configDir);
    if (!configDirFile.exists()) {
      log.warn("数据源配置目录不存在 {}", configDirFile);
      return;
    }

    for (File file : Files.fileTraverser().breadthFirst(configDirFile)) {
      String name = file.getName();
      if (file.isFile() && name.endsWith(".properties")) {
        try {
          initDatasource(name.replaceAll(".properties$", ""));
        } catch (SQLException | ManagedDatasourceException e) {
          log.warn("初始化数据源出错：" + name, e);
        }
      }
    }
  }

  @Transactional
  public void initDatasource(String dsName)
      throws SQLException, ManagedDatasourceException {
    removeDatasource(dsName);
    ManagedDatasource.DatasourceConfig dsConfig = managedDatasource.getDsConfig(dsName);
    String dsType = dsConfig.getType();
    String dsId = insertDatasource(dsName, dsType);

    MetadataRetreiver metadataRetreiver = MetadataRetreiverBuilder.of(dsType);

    try (Connection connection = managedDatasource.getConnection(dsName)) {
      Optional<String> schemaName = metadataRetreiver.getCurrentSchema(connection);
      if (schemaName.isPresent()) {
        initSchema(metadataRetreiver, connection, dsId, dsType, null, schemaName.orElse(null));
      } else {
        Set<String> catalogs = metadataRetreiver.getCatalogs(connection);
        if (catalogs == null) {
          log.info("catalogs=null");
          catalogs = new HashSet<>();
          catalogs.add(null);
        }
        for (String catalog : catalogs) {
          Set<String> schemaNames = metadataRetreiver.getSchemaNames(connection, catalog);
          for (String n : schemaNames) {
            if (!"information_schema".equals(n)) {
              initSchema(metadataRetreiver, connection, dsId, dsType, null, n);
            }
          }
        }
      }
    }
  }

  public void initSchema(MetadataRetreiver metadataRetreiver, Connection connection, String dsId, String dsType,
                         String catalogName, String schemaName) throws SQLException {
    // 获取各个数据源下的表信息
    insertSchema(dsId, schemaName, dsType);

    for (String table : metadataRetreiver.getTables(connection, null, schemaName)) {
      initTable(metadataRetreiver, connection, dsId, catalogName, schemaName, table);
    }
  }

  public void initTable(MetadataRetreiver metadataRetreiver, Connection connection, String dsId, String catalogName, String schemaName, String tableName) throws SQLException {
    insertTable(dsId, schemaName, tableName);

    for (MetadataRetreiver.ColumnInfo column : metadataRetreiver.getColumns(connection, catalogName, schemaName, tableName)) {
      insertColumn(dsId, schemaName, tableName, column);
    }
  }


  /**
   * 插入数据源信息
   */
  @Transactional
  String insertDatasource(String dsName, String dsType) {
    log.trace("insertDatasource {} {}", dsName, dsType);
    DcDatasource ds = new DcDatasource();
    ds.setId(UUIDUtils.getUUID());
    ds.setCreateDatetime(new Date());
    ds.setDataSourceName(dsName);
    ds.setDataSourceConfig(dsName);
    ds.setDataSourceType(dsType);
    ds.setStatus(ApplicationConstants.STATUS_NORMAL);
    ds.setId(dsName);
    dcDatasourceMapper.insert(ds);
    return ds.getId();
  }

  @Transactional
  protected void insertSchema(String dsId, String schemaName, String dsType) {
    DcDataSchema dcSchema = new DcDataSchema();
    log.trace("insertSchema {}", schemaName);
    dcSchema.setDatabaseName(schemaName);
    dcSchema.setId(UUIDUtils.getUUID());
    dcSchema.setDatabaseSort(dsType);
    dcSchema.setCreateDatetime(new Date());
    dcSchema.setModifyDatetime(new Date());
    dcSchema.setDataSourceId(dsId);
    dcSchema.setRemark("");
    dcDataSchemaMapper.insert(dcSchema);
  }

  @Transactional
  protected void insertTable(String dsId, String schemaName, String tableName) {
    log.trace("insertTable {}", tableName);
    DcDataTable table = new DcDataTable();
    table.setDataBaseName(schemaName);
    table.setDataSourceId(dsId);
    table.setCreateDatetime(new Date());
    table.setRemark("暂无备注");
    table.setTableName(tableName);
    dcDataTableMapper.insert(table);
  }

  @Transactional
  protected void insertColumn(String dsId, String schemaName, String tableName, MetadataRetreiver.ColumnInfo columnInfo) {
    log.trace("insertColumn {}", columnInfo);
    DcDataColumn record = new DcDataColumn();
    record.setTableName(tableName);
    record.setDataBaseName(schemaName);
    record.setColumnType(columnInfo.getType());
    record.setColumnName(columnInfo.getName());
    record.setCreateDatetime(new Date());
    record.setDataSourceId(dsId);
    dcDataColumnMapper.insert(record);
  }
}
