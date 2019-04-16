package com.zhuxun.dc.apirunner.service.datasource;

import com.google.common.io.Files;
import com.zhuxun.dc.apirunner.constant.StatusConstant;
import com.zhuxun.dc.apirunner.dao.entity.*;
import com.zhuxun.dc.apirunner.dao.mapper.extra.DataSourceMapper;
import com.zhuxun.dc.apirunner.entity.vo.datasource.VDataSource;
import com.zhuxun.dc.apirunner.exception.ManagedDatasourceException;
import com.zhuxun.dc.apirunner.service.BaseService;
import com.zhuxun.dc.apirunner.service.ManagedDatasourceService;
import com.zhuxun.dc.apirunner.service.datasource.metadata.MetadataRetreiver;
import com.zhuxun.dc.apirunner.service.datasource.metadata.MetadataRetreiverBuilder;
import com.zhuxun.dc.apirunner.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;

/**
 * 数据源初始化服务
 *
 * @author tao
 */
@Service
@Slf4j
public class DatasourceInitService extends BaseService {

  @Autowired
  protected ManagedDatasourceService managedDatasourceService;

  @Autowired
  protected DataSourceMapper dataSourceMapper;

  @Transactional
  public void removeAllDatasource() {
    List<DcDataSource> dcDataSources = dcDataSourceMapper.selectByExample(new DcDataSourceExample());
    for (DcDataSource dataSource : dcDataSources) {
      removeDatasource(dataSource.getDataSourceName());
    }
  }

  /**
   * 根据配置文件的名称删除所有的数据库和表以及字段
   */
  @Transactional
  public void removeDatasource(String dsName) {
    log.info("移除数据源 {}", dsName);
    VDataSource ds = dataSourceMapper.selectDataSourceByName(dsName);
    if (ds != null && ds.getId() != null) {
      dataSourceMapper.deleteColumnByDatasourceId(ds.getId());
      dataSourceMapper.deleteTableByDatasourceId(ds.getId());
      dataSourceMapper.deleteSchemaByDatasourceId(ds.getId());
      dataSourceMapper.deleteDataSourceByName(dsName);
    }
  }

  @Transactional
  public void initAllDatasource() throws SQLException, ManagedDatasourceException {
    removeAllDatasource();
    for (File file : Files.fileTraverser().breadthFirst(new File(configDir))) {
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
    Properties dsProperty = managedDatasourceService.getDatasourceProperties(dsName);
    String dsType = dsProperty.getProperty(ManagedDatasourceService.CONNECTION_NAME);
    String dsId = insertDatasource(dsName, dsType);

    MetadataRetreiver metadataRetreiver = MetadataRetreiverBuilder.of(dsType);

    try (Connection connection = managedDatasourceService.getConnection(dsName)) {
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
   *
   * @param dsName
   * @param dsType
   */
  @Transactional
  String insertDatasource(String dsName, String dsType) {
    log.trace("insertDatasource {} {}", dsName, dsType);
    DcDataSource ds = new DcDataSource();
    ds.setId(UUIDUtils.getUUID());
    ds.setCreateDatetime(new Date());
    ds.setCreateUserid(getUserId());
    ds.setDataSourceName(dsName);
    ds.setDataSourceConfig(dsName);
    ds.setDataSourceType(dsType);
    ds.setStatus(StatusConstant.STATUS_NORMAL);
    ds.setId(dsName);
    dcDataSourceMapper.insert(ds);
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
    dcSchema.setDatabaseSort(getUserId());
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
    table.setCreateUserid(getUserId());
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
    record.setCreateUserid(getUserId());
    record.setDataSourceId(dsId);
    dcDataColumnMapper.insert(record);
  }
}
