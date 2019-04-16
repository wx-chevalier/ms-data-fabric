package com.zhuxun.dmc.apim.service;

import com.zhuxun.dmc.apim.dto.api.apiimpl.APIImplTO;
import com.zhuxun.dmc.sqlapi.errors.ManagedDatasourceException;
import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import io.swagger.v3.oas.models.media.Schema;

import java.sql.SQLException;
import java.util.Map;

/**
 * 有状态的实现：
 * - parser: 解析实现，解析后的状态应该被存储
 */
public interface APIImplDevTool {
  APIImplDevTool parse(APIImplTO impl)
      throws ManagedDatasourceException, SQLAPIException, SQLException;

  Boolean getIsQuery();

  Schema getRequestBodySchema();

  Schema getResponseSchema();

  Object execute(Map<String, Object> param) throws ManagedDatasourceException, SQLAPIException, SQLException;
}
