package com.zhuxun.dmc.apim.service.impl;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.zhuxun.dmc.apim.domain.datasource.Column;
import com.zhuxun.dmc.apim.domain.datasource.Datasource;
import com.zhuxun.dmc.apim.domain.datasource.Schema;
import com.zhuxun.dmc.apim.domain.datasource.Table;
import com.zhuxun.dmc.apim.repository.model.*;
import com.zhuxun.dmc.apim.service.AbstractService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.google.common.collect.Iterables.getOnlyElement;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@Slf4j
public class DatasourceService extends AbstractService {
  /**
   * 根据数据库名称查询
   *
   * @param dataSchemaName 数据源名称
   * @return
   */
  @Transactional(readOnly = true)
  public List<Schema> getDatasourceById(String dataSchemaName, String dataSourceId) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(dataSourceId), "查询数据源库失败,数据源id不能为空");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(dataSchemaName), "查询数据源库失败,数据库名称不能为空");

    DcDatasource datasource = dcDatasourceMapper.selectByPrimaryKey(dataSourceId);

    if (datasource == null) {
      return new ArrayList<>();
    }

    DcDataSchemaExample example = new DcDataSchemaExample();
    example.createCriteria()
        .andDatabaseNameEqualTo(dataSchemaName)
        .andDataSourceIdEqualTo(dataSourceId);
    return dcDataSchemaMapper.selectByExample(example).stream()
        .map(s -> s.withDatabaseSort(datasource.getDataSourceType()))
        .map(Schema::of)
        .collect(toList());
  }

  /**
   * 获取用户可用的数据库列表
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Collection<Schema> getSchemaList(String userId) {
    List<DcDatasource> datasources = dcDatasourceMapper.selectByExample(new DcDatasourceExample());
    if (datasources.isEmpty()) {
      return new ArrayList<>();
    }
    Map<String, String> mapDsIdType = new HashMap<>();
    Map<String, String> mapDsIdName = new HashMap<>();
    List<String> dsIds = new ArrayList<>();
    for (DcDatasource datasource : datasources) {
      mapDsIdType.put(datasource.getId(), datasource.getDataSourceType());
      mapDsIdName.put(datasource.getId(), datasource.getDataSourceName());
      dsIds.add(datasource.getId());
    }

    if (dsIds.size() == 0) {
      return new ArrayList<>();
    }

    DcDataSchemaExample schemaExample = new DcDataSchemaExample();
    schemaExample.createCriteria().andDataSourceIdIn(dsIds);

    return dcDataSchemaMapper.selectByExample(schemaExample).stream()
        .map(s -> s.withDatabaseSort(mapDsIdType.getOrDefault(s.getDataSourceId(), null)))
        .map(Schema::of)
        .peek(s -> {
          Datasource ds = s.getDataSource();
          if (ds != null) {
            ds.setName(mapDsIdName.getOrDefault(ds.getId(), null));
          }
        })
        .collect(toList());
  }

  /**
   * 获取指定数据库表信息
   *
   * @param schemaName     数据库名
   * @param datasourceName 数据源名
   * @return
   */
  @Transactional(readOnly = true)
  public Collection<Table> getTableList(String schemaName, String datasourceName) {
    // TODO 验证用户可使用该数据库
    DcDatasourceExample datasourceExample = new DcDatasourceExample();
    datasourceExample.createCriteria().andDataSourceNameEqualTo(datasourceName);
    // 获取数据源信息
    DcDatasource datasource = getOnlyElement(dcDatasourceMapper.selectByExample(datasourceExample), null);
    if (datasource == null) {
      return new ArrayList<>();
    }

    // 获取指定数据库中所有列
    DcDataColumnExample columnExample = new DcDataColumnExample();
    columnExample.createCriteria()
        .andDataSourceIdEqualTo(datasource.getId())
        .andDataBaseNameEqualTo(schemaName);

    List<DcDataColumn> columns = dcDataColumnMapper.selectByExample(columnExample);
    Map<String, List<DcDataColumn>> mapTableNameColumns = columns
        .stream()
        .collect(groupingBy(DcDataColumn::getTableName));

    List<Table> tableList = new ArrayList<>();
    for (Map.Entry<String, List<DcDataColumn>> entry : mapTableNameColumns.entrySet()) {
      tableList.add(new Table()
          .setName(entry.getKey())
          .setColumnList(entry.getValue().stream().map(Column::of).collect(toList())));
    }

    return tableList;
  }
}
