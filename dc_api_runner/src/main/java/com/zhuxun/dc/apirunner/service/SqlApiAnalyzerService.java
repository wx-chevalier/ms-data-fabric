package com.zhuxun.dc.apirunner.service;

import com.zhuxun.dc.apirunner.constant.PermissionConstant;
import com.zhuxun.dc.apirunner.constant.RoleConstant;
import com.zhuxun.dc.apirunner.entity.dto.SqlAnalysis;
import com.zhuxun.dc.apirunner.entity.dto.SqlExecution;
import com.zhuxun.dc.apirunner.entity.vo.VZuulRoute;
import com.zhuxun.dc.apirunner.exception.ManagedDatasourceException;
import com.zhuxun.dc.apirunner.exception.SQLAPIException;
import com.zhuxun.dc.apirunner.sqlapi.SQLAPIAnalyzer;
import com.zhuxun.dc.apirunner.sqlapi.impl.SQLAPIAnalyzerImpl;
import com.zhuxun.dc.apirunner.sqlapi.impl.SQLAPIExecutorImpl;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * SQL语句分析Service
 *
 * @author tao
 */
@Service
@Slf4j
public class SqlApiAnalyzerService extends BaseService {
  private ManagedDatasourceService managedDatasourceService;

  public SqlApiAnalyzerService(@Autowired ManagedDatasourceService managedDatasourceService) {
    this.managedDatasourceService = managedDatasourceService;
  }

  /**
   * 分析Sql参数
   *
   * @return
   */
  @RequiresRoles(value = {RoleConstant.ADMIN_NAME})
  @RequiresPermissions(value = {PermissionConstant.CREATE_API})
  public Schema analyzeParameter(SqlAnalysis sqlAnalysis) throws SQLAPIException, SQLException {
    checkArgument(!isNullOrEmpty(sqlAnalysis.getDialect()), "数据源类型为空");
    checkArgument(!isNullOrEmpty(sqlAnalysis.getSql()), "空的 SQL 语句");
    if (!"presto".equals(sqlAnalysis.getDialect())) {
      checkArgument(
          !isNullOrEmpty(sqlAnalysis.getDatasourceName()), "数据源未指定");
    }
    return new SQLAPIAnalyzerImpl()
        .analyzeParameter(
            sqlAnalysis.getSql(),
            sqlAnalysis.getDialect());
  }

  /**
   * 执行测试Sql
   */
  @RequiresRoles(value = {RoleConstant.ADMIN_NAME})
  @RequiresPermissions(value = {PermissionConstant.CREATE_API})
  public Object execute(SqlExecution sqlExecution)
      throws SQLException, SQLAPIException, ManagedDatasourceException {
    // 校验Sql
    ObjectSchema parameterSchema = sqlExecution.getParameterSchema();
    String dialect = sqlExecution.getDialect();
    String sql = sqlExecution.getSql();

    checkArgument(!isNullOrEmpty(dialect), "数据源类型为空");
    checkArgument(!isNullOrEmpty(sql), "空的 SQL 语句");
    checkArgument(parameterSchema != null, "空的参数 Schema");
    if (!"presto".equals(dialect)) {
      checkArgument(
          !isNullOrEmpty(sqlExecution.getDatasourceName()), "数据源未指定");
    }

    // 获取参数值
    Map<String, Object> parameter = new HashMap<>();
    for (Map.Entry<String, Schema> entry : parameterSchema.getProperties().entrySet()) {
      parameter.put(entry.getKey(), entry.getValue().getExample());
    }

    try (Connection connection =
             managedDatasourceService.getConnection(sqlExecution.getDatasourceName())) {
      SQLAPIAnalyzer analyzer = new SQLAPIAnalyzerImpl()
          .parameter(parameterSchema)
          .cnx(connection);
      SQLAPIExecutorImpl executor = new SQLAPIExecutorImpl()
          .cnx(connection);

      // 分析语句成分
      Schema analyzeResponse = analyzer.analyzeResponse(sql, dialect);

      // 判断是是否是查询操作
      if (analyzeResponse == SQLAPIAnalyzerImpl.UPDATE_RESULT_SCHEMA) {
        return executor
            .rollbackAfterExecute(true)
            .execute(sql, dialect, parameter, parameterSchema);

      } else {
        return executor.executeQuery(sql, dialect, parameter, parameterSchema);
      }
    }
  }


  /**
   * 获取所有的接口(外部接口)列表
   *
   * @return
   */
  public List<VZuulRoute> getAllRoute() {
    List<VZuulRoute> allRoute = apiManageMapper.getAllRoute();
    return allRoute;
  }
}
