package com.zhuxun.dmc.apim.repository;

import com.zhuxun.dmc.apim.domain.datasource.Datasource;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface DataSourceMapper {
  @Select(
      "SELECT id, data_source_name AS type\n" +
          "FROM dc_data_source\n" +
          "WHERE data_source_name=#{dsName}")
  Datasource selectDataSourceByName(@Param("dsName") String dsName);

  @Delete("DELETE FROM dc_data_source WHERE data_source_name=#{dsName}")
  void deleteDataSourceByName(@Param("dsName") String dsName);

  @Delete("DELETE FROM dc_data_schema WHERE data_source_id=#{dsId}")
  void deleteSchemaByDatasourceId(@Param("dsId") String dsId);

  @Delete("DELETE FROM dc_data_table WHERE data_source_id=#{dsId}")
  void deleteTableByDatasourceId(@Param("dsId") String dsId);

  @Delete("DELETE FROM dc_data_column WHERE data_source_id=#{dsId}")
  void deleteColumnByDatasourceId(@Param("dsId") String dsId);

  @Select(
      "SELECT\n" +
          "  dc_data_source.data_source_name as sourcesName,\n" +
          "  dc_data_source.data_source_type as sourcesType,\n" +
          "  dc_data_schema.database_name as databaseName\n" +
          "FROM dc_data_source\n" +
          "  LEFT JOIN dc_data_schema ON dc_data_source.data_source_name = dc_data_schema.data_source_id")
  List<Map<String, String>> getDataSchemaList();

  @Select(
      "SELECT\n" +
          "  table_name  as tableName,\n" +
          "  column_name as columnName,\n" +
          "  column_type as columnType\n" +
          "FROM dc_data_column\n" +
          "WHERE data_source_id = #{dsName} AND data_base_name = #{shemaName}")
  List<Map<String, String>> getSchemaStruct(
      @Param("dsName") String dsName, @Param("schemaName") String schema);
}
