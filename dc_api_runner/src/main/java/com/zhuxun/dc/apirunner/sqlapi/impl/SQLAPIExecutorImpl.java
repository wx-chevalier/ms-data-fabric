package com.zhuxun.dc.apirunner.sqlapi.impl;

import com.zhuxun.dc.apirunner.exception.SQLAPIException;
import com.zhuxun.dc.apirunner.exception.SchemaValidationError;
import com.zhuxun.dc.apirunner.sqlapi.SQLAPiExecutor;
import com.zhuxun.dc.apirunner.sqlapi.SQLValidation;
import com.zhuxun.dc.apirunner.sqlapi.VariableParser;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Accessors(fluent = true, chain = true)
@Slf4j
public class SQLAPIExecutorImpl implements SQLAPiExecutor {
  private static final SimpleDateFormat DATE_FORMAT
      = new SimpleDateFormat("yyyy-MM-dd");

  private static final SimpleDateFormat DATE_TIME_FORMAT
      = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");

  /**
   * 要执行的 SQL 对应的数据库 JDBC 连接
   */
  @Getter
  @Setter
  Connection cnx;

  /**
   * 如果 true, 执行更新、插入操作之后，自动 rollback ，用于测试这类语句
   */
  @Getter
  @Setter
  Boolean rollbackAfterExecute = false;

  @Override
  public Collection<Map<String, Object>> executeQuery(
      String sql,
      String dialect,
      Map<String, Object> parameter,
      Schema parameterSchema
  ) throws SQLAPIException, SQLException {
    if (!(parameterSchema instanceof ObjectSchema)) {
      throw new SQLAPIException(
          "Invalid validatedParam schema: Object schema required");
    }
    log.debug("SQL={}, dialect={}, parameter={}, schema={}", sql, dialect, parameter, parameterSchema);
    // 使用 Schema 验证参数
    Map<String, Object> validatedParam = validateParameter(
        parameter, (ObjectSchema) parameterSchema);

    // 解析 SQL
    VariableParser parser = new VariableParser(sql).parse();

    // 验证
    new SQLValidation().validate(parser.parsedSql(), dialect);

    // 构造查询语句
    PreparedStatement preparedStmt = preparedStatement(
        validatedParam, parser.positionedVariables(), parser.questionMarkedSql());

    // 执行查询
    ResultSet resultSet = preparedStmt.executeQuery();
    ResultSetMetaData metaData = resultSet.getMetaData();
    final int columnCount = metaData.getColumnCount();

    // 生成结果
    List<Map<String, Object>> result = new ArrayList<>();
    while (resultSet.next()) {
      Map<String, Object> item = new HashMap<>();
      for (int i = 1; i <= columnCount; i++) {
        item.put(
            metaData.getColumnLabel(i),
            resultSet.getObject(i));
      }
      result.add(item);
    }

    return result;
  }

  @Override
  public UpdateResult execute(
      String sql,
      String dialect,
      Map<String, Object> parameter,
      Schema parameterSchema) throws SQLException, SQLAPIException {
    if (!(parameterSchema instanceof ObjectSchema)) {
      throw new SQLAPIException(
          "Invalid validatedParam schema: Object schema required");
    }
    // 使用 Schema 验证参数
    Map<String, Object> validatedParam =
        validateParameter(parameter, (ObjectSchema) parameterSchema);

    // 解析 SQL
    VariableParser parser = new VariableParser(sql).parse();

    // 验证
    new SQLValidation().validate(parser.parsedSql(), dialect);

    PreparedStatement preparedStmt = preparedStatement(
        validatedParam, parser.positionedVariables(), parser.questionMarkedSql());

    cnx.setAutoCommit(false);
    int affected = preparedStmt.executeUpdate();
    if (rollbackAfterExecute) {
      cnx.rollback();
    } else {
      cnx.commit();
    }
    return new UpdateResult().affected(affected);
  }

  /**
   * 创建 PreparedStatement
   */
  private PreparedStatement preparedStatement(
      Map<String, Object> parameter,
      List<String> positionedVariables,
      String questionMarkedSql) throws SQLException, SQLAPIException {
    int i = 0;
    PreparedStatement stmt = cnx.prepareStatement(questionMarkedSql);
    for (String var : positionedVariables) {
      i++;
      if (!parameter.containsKey(var)) {
        throw new SQLAPIException("Inconsistent schema");
      } else {
        stmt.setObject(i, parameter.get(var));
      }
    }
    return stmt;
  }

  /**
   * 使用 Schema 验证参数
   *
   * @param parameter       参数
   * @param parameterSchema SQL 解析出的参数的 ObjectSchema
   * @throws SchemaValidationError 参数验证出错
   */
  private Map<String, Object> validateParameter(
      Map<String, Object> parameter,
      ObjectSchema parameterSchema
  ) throws SchemaValidationError {
    Map<String, Object> resultParameter = new HashMap<>();
    Map<String, Schema> propertySchemas = parameterSchema.getProperties();

    for (Map.Entry<String, Object> entry : parameter.entrySet()) {
      Schema propertySchema =
          propertySchemas.getOrDefault(entry.getKey(), null);

      if (propertySchema == null) {
        throw new SchemaValidationError(
            "未知属性: " + entry.getKey());
      }
      try {
        resultParameter.put(
            entry.getKey(),
            validatePrimitive(entry.getValue(), propertySchema));
      } catch (SchemaValidationError e) {
        throw new SchemaValidationError(
            String.format(
                "属性验证失败[%s]: %s", entry.getKey(), e.getMessage()),
            e);
      }
    }

    return resultParameter;
  }

  /**
   * 基本类型值的验证
   */
  private Object validatePrimitive(
      Object val,
      Schema schema
  ) throws SchemaValidationError {
    switch (schema.getType()) {
      case "number":
        if (!(val instanceof Number)) {
          throw new SchemaValidationError("不是 number: " + val);
        } else {
          return val;
        }
      case "string":
        if (!(val instanceof String)) {
          throw new SchemaValidationError("不是 string: " + val);
        }
        if (schema.getFormat() == null) {
          return val;
        }
        switch (schema.getFormat()) {
          case "date":
            try {
              return DATE_FORMAT.parse((String) val);
            } catch (ParseException e) {
              throw new SchemaValidationError(
                  "不是合法格式的 date (yyyy-MM-dd): " + val);
            }
          case "date-time":
            try {
              return DATE_TIME_FORMAT.parse((String) val);
            } catch (ParseException e) {
              throw new SchemaValidationError(
                  "不是合法格式的 date-time(yyyy-MM-ddTHH:mm:ssXXX): " + val);
            }
          default:
            return val;
        }
      case "integer":
        if (!(val instanceof Integer)) {
          throw new SchemaValidationError("不是整数: " + val);
        }
        return val;
      case "boolean":
        if (!(val instanceof Boolean)) {
          throw new SchemaValidationError("不是布尔值: " + val);
        }
        return val;
      default:
        throw new SchemaValidationError("不支持的类型: " + schema.getType());
    }
  }
}
