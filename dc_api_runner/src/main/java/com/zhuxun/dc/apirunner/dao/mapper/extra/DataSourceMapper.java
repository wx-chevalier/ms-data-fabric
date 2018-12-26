package com.zhuxun.dc.apirunner.dao.mapper.extra;

import com.zhuxun.dc.apirunner.entity.vo.datasource.VDataSource;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface DataSourceMapper {
  VDataSource selectDataSourceByName(@Param("dsName") String dsName);

  void deleteDataSourceByName(@Param("dsName") String dsName);

  void deleteSchemaByDatasourceId(@Param("dsId") String dsId);

  void deleteTableByDatasourceId(@Param("dsId") String dsId);

  void deleteColumnByDatasourceId(@Param("dsId") String dsId);
}
