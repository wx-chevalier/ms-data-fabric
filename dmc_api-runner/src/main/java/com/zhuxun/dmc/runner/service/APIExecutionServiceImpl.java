package com.zhuxun.dmc.runner.service;

import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.api.apiimpl.APIImpl;
import com.zhuxun.dmc.apim.domain.api.apiimpl.APIImplDef;
import com.zhuxun.dmc.apim.domain.api.apiimpl.SQLImplDef;
import com.zhuxun.dmc.apim.domain.api.apiimpl.graph.RelGraphImplDef;
import com.zhuxun.dmc.runner.service.errors.InCompleteDefinitionError;
import com.zhuxun.dmc.sqlapi.datasource.ManagedDatasource;
import com.zhuxun.dmc.sqlapi.errors.ManagedDatasourceException;
import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import com.zhuxun.dmc.sqlapi.impl.SQLAPIExecutorImpl;
import com.zhuxun.spring.service.errors.NotSupportedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.Map;

@Service
@Slf4j
public class APIExecutionServiceImpl implements APIExecutionService {
  private ManagedDatasource managedDatasource;

  @Autowired
  public APIExecutionServiceImpl(ManagedDatasource managedDatasource) {
    this.managedDatasource = managedDatasource;
  }

  @Override
  public Object execute(API api, Map<String, Object> param)
      throws InCompleteDefinitionError, ManagedDatasourceException, SQLAPIException, SQLException {
    APIImpl impl = api.getImpl();
    if (impl == null || impl.getImpl() == null) {
      throw new InCompleteDefinitionError();
    }
    APIImplDef implDef = impl.getImpl();

    if (implDef instanceof RelGraphImplDef) {
      ((RelGraphImplDef) implDef).getCompiled();
    }

    if (implDef instanceof SQLImplDef || implDef instanceof RelGraphImplDef) {
      SQLImplDef def;
      if (implDef instanceof SQLImplDef) {
        def = (SQLImplDef) implDef;
      } else {
        def = ((RelGraphImplDef) implDef).getCompiled();
        if (def == null) {
          throw new InCompleteDefinitionError("Graph defined API not compiled");
        }
      }
      SQLAPIExecutorImpl sqlapiExecutor =
          new SQLAPIExecutorImpl()
              .setDataSource(managedDatasource.getDataSource(def.getDatasourceName()));
      if (def.getIsQuery()) {
        return sqlapiExecutor.executeQuery(
            def.getSql(), def.getDialect(), param, api.getRequestBody().getSchema());
      } else {
        return sqlapiExecutor.execute(
            def.getSql(), def.getDialect(), param, api.getRequestBody().getSchema());
      }
    } else {
      log.warn("接口定义不受支持: {}", implDef);
      throw new NotSupportedException("接口定义不受支持");
    }
  }
}
