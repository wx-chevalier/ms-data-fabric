package com.zhuxun.dc.apirunner.sqlapi.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.google.common.base.Preconditions;
import com.zhuxun.dc.apirunner.exception.SQLAPIException;
import com.zhuxun.dc.apirunner.sqlapi.SQLAPIAnalyzer;
import com.zhuxun.dc.apirunner.sqlapi.SQLValidation;
import com.zhuxun.dc.apirunner.sqlapi.Utils;
import com.zhuxun.dc.apirunner.sqlapi.VariableParser;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

@Accessors(fluent = true, chain = true)
@Slf4j
public class SQLAPIAnalyzerImpl implements SQLAPIAnalyzer {
  /**
   * 非查询接口返回 schema
   */
  public final static ObjectSchema UPDATE_RESULT_SCHEMA = new ObjectSchema();

  static {
    Map<String, Schema> properties = new HashMap<>();
    properties.put("affected", new IntegerSchema());
    UPDATE_RESULT_SCHEMA.setProperties(properties);
  }

  /**
   * 要解析的 SQL 对应的数据库 JDBC 连接
   */
  @Getter
  @Setter
  Connection cnx;

  /**
   * 要解析的 SQL 对应的包含示例值的参数 Schema
   */
  @Getter
  @Setter
  ObjectSchema parameter;

  @Override
  public Schema analyzeParameter(String sql, String dialect) throws SQLAPIException, SQLException {
    VariableParser parser = new VariableParser(sql).parse();
    // 验证 SQL
    new SQLValidation().validate(parser.parsedSql(), dialect);

    // 生成 sql 对应请求 Schema
    ObjectSchema schema = new ObjectSchema();

    Map<String, Schema> properties = new HashMap<>();
    for (String var : new HashSet<>(parser.positionedVariables())) {
      StringSchema varSchema = new StringSchema();
      properties.put(var, varSchema);
    }

    schema.setProperties(properties);
    return schema;
  }

  @Override
  public Schema analyzeResponse(String sql, String dialect) throws SQLAPIException, SQLException {
    VariableParser parser = new VariableParser(sql).parse();

    // 验证 SQL
    new SQLValidation().validate(parser.parsedSql(), dialect);

    if (!"presto".equals(dialect)) {
      // 非 presto 时支持除查询语句之外的语句
      List<SQLStatement> stmts = SQLUtils.parseStatements(parser.parsedSql(), dialect);
      // 通过验证，应该有且只有一条语句
      Preconditions.checkState(stmts.size() == 1, "不支持多条语句");

      SQLStatement stmt = stmts.get(0);
      if (!(stmt instanceof SQLSelectStatement)) {
        // 非查询语句返回模型
        return UPDATE_RESULT_SCHEMA;
      }
    }

    // 生成 sql 对应返回值 Schema
    PreparedStatement preparedStmt = cnx.prepareStatement(parser.questionMarkedSql());
    Map<String, Object> cachedExampleValue = new HashMap<>();

    log.debug("Preparing {}", parser.questionMarkedSql());
    {
      int i = 0;
      for (String var : parser.positionedVariables()) {
        i++;
        if (!cachedExampleValue.containsKey(var)) {
          cachedExampleValue.put(var, this.getExampleValue(var));
        }
        preparedStmt.setObject(i, cachedExampleValue.get(var));
        log.debug("Set parameter {} -> {}", i, cachedExampleValue.get(var));
      }
    }

    ResultSet resultSet = preparedStmt.executeQuery();
    ResultSetMetaData metaData = resultSet.getMetaData();

    ObjectSchema resultSchema = new ObjectSchema();

    Map<String, Schema> properties = new HashMap<>();
    for (int i = 1; i <= metaData.getColumnCount(); i++) {
      String name = metaData.getColumnLabel(i);
      int columnType = metaData.getColumnType(i);
      properties.put(name, Utils.convertToSchema(columnType));
    }

    resultSchema.setProperties(properties);

    return resultSchema;
  }

  /**
   * 获取指定变量的示例值
   */
  private Object getExampleValue(String var) {
    if (this.parameter == null) {
      return null;
    }
    Map<String, Schema> properties = this.parameter.getProperties();
    Schema schema = properties.getOrDefault(var, null);
    if (schema == null) {
      return null;
    }
    return schema.getExample();
  }
}
