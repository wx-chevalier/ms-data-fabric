package com.zhuxun.dmc.apim.service.impl.manager.api;

import com.zhuxun.dmc.apim.dto.api.apiimpl.APIImplTO;
import com.zhuxun.dmc.apim.dto.api.apiimpl.SQLImpl;
import com.zhuxun.dmc.apim.service.APIImplDevTool;
import com.zhuxun.dmc.apim.service.errors.NotSupportedException;
import com.zhuxun.dmc.sqlapi.SQLAPIAnalyzer;
import com.zhuxun.dmc.sqlapi.SQLAPIExecutor;
import com.zhuxun.dmc.sqlapi.datasource.ManagedDatasource;
import com.zhuxun.dmc.sqlapi.errors.ManagedDatasourceException;
import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import com.zhuxun.dmc.sqlapi.impl.SQLAPIAnalyzerImpl;
import com.zhuxun.dmc.sqlapi.impl.SQLAPIExecutorImpl;
import io.swagger.v3.oas.models.media.Schema;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static com.google.common.base.Preconditions.checkState;

@Accessors(chain = true)
@Slf4j
public class APIImplDevToolImpl implements APIImplDevTool {
  private ManagedDatasource managedDatasource;
  private Schema paramSchema = null;
  private Schema respSchema = null;
  private APIImplTO parsedImpl;
  private SQLAPIAnalyzer analyzer;

  public APIImplDevToolImpl(ManagedDatasource managedDatasource) {
    this.managedDatasource = managedDatasource;
  }

  @Override
  public Boolean getIsQuery() {
    checkState(analyzer != null, "未解析任何接口实现");
    return analyzer.getIsQuery();
  }

  @Override
  public APIImplDevTool parse(APIImplTO apiImpl)
      throws ManagedDatasourceException, SQLAPIException, SQLException {
    this.parsedImpl = apiImpl;
    if (apiImpl instanceof SQLImpl) {
      SQLImpl impl = (SQLImpl) apiImpl;
      analyzer = createAnalyzer(impl.getDatasourceName());
      paramSchema = analyzer.analyzeParameter(impl.getSql(), impl.getDialect());
      respSchema = analyzer.analyzeResponse(
          impl.getSql(), impl.getDialect(), new HashMap<>());
    } else {
      throw new NotSupportedException("关系图方式的接口定义尚未实现");
    }
    return this;
  }

  @Override
  public Schema getRequestBodySchema() {
    checkState(analyzer != null, "未解析任何接口实现");
    return paramSchema;
  }

  @Override
  public Schema getResponseSchema() {
    checkState(analyzer != null, "未解析任何接口实现");
    return respSchema;
  }

  @Override
  public Object execute(Map<String, Object> param) throws ManagedDatasourceException, SQLAPIException, SQLException {
    checkState(analyzer != null && parsedImpl != null, "未解析任何接口实现");

    if (parsedImpl instanceof SQLImpl) {
      SQLImpl impl = (SQLImpl) parsedImpl;
      SQLAPIExecutor executor = createExecutor(impl.getDatasourceName());
      if (getIsQuery()) {
        return executor.executeQuery(
            impl.getSql(), impl.getDialect(), param, getRequestBodySchema());
      } else {
        return executor.execute(
            impl.getSql(), impl.getDialect(), param, getRequestBodySchema());
      }
    }
    return null;
  }

  private Connection getConnection(String dsName) throws ManagedDatasourceException {
    return managedDatasource.getConnection(dsName);
  }

  private SQLAPIAnalyzer createAnalyzer(String dsName) throws ManagedDatasourceException {
    return new SQLAPIAnalyzerImpl().setConnection(getConnection(dsName));
  }

  private SQLAPIExecutor createExecutor(String dsName) throws ManagedDatasourceException {
    return new SQLAPIExecutorImpl().setConnection(getConnection(dsName));
  }
}
