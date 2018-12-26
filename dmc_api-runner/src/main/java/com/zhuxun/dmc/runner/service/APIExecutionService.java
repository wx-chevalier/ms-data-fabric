package com.zhuxun.dmc.runner.service;

import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.runner.service.errors.InCompleteDefinitionError;
import com.zhuxun.dmc.sqlapi.errors.ManagedDatasourceException;
import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Map;

public interface APIExecutionService {
  Object execute(API api, Map<String, Object> param) throws InCompleteDefinitionError, ManagedDatasourceException, SQLAPIException, SQLException;
}
