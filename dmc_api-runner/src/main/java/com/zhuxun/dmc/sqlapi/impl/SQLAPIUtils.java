package com.zhuxun.dmc.sqlapi.impl;

import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import com.zhuxun.dmc.sqlapi.utils.SQLAPIConstants;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import static java.lang.String.format;

public class SQLAPIUtils {
  public enum DefaultValueType {
    DEFAULT, // 使用 Schema 中定义的默认值
    EXAMPLE_OR_GENERATED // 使用 Schema 中定一的 Example 值，或者按类型生成
  }

  public static PreparedStatement prepareStmt(
      Connection connection,
      VariableParser variableParser,
      Schema paramSchema,
      Map<String, Object> paramValues,
      DefaultValueType defaultValueType)
      throws SQLException, SQLAPIException {

    PreparedStatement preparedStmt =
        connection.prepareStatement(variableParser.getQuestionMarkedSql());

    int i = 0;
    for (VariableParser.Var var : variableParser.positionedVariables) {
      i++;
      preparedStmt.setObject(
          i,
          SQLAPIUtils.getExampleValue(
              var.getName(), paramSchema, paramValues, defaultValueType));
    }

    return preparedStmt;
  }

  /** @param schema 假定属性值都为简单类型 */
  public static Object getExampleValue(
      String var,
      Schema schema,
      Map<String, Object> paramValues,
      DefaultValueType defaultValueType)
      throws SQLAPIException {

    Map<String, Schema> properties = schema.getProperties();
    if (properties == null || !properties.containsKey(var)) {
      throw new SQLAPIException("未知变量: " + var);
    }
    Schema propertySchema = properties.get(var);

    Object v;
    if (paramValues.containsKey(var)) {
      v = paramValues.getOrDefault(var, null);
    } else {
      switch (defaultValueType) {
        case DEFAULT:
          if (propertySchema.getDefault() == null) {
            throw new SQLAPIException(format("参数值未设定：%s", var));
          } else {
            v = propertySchema.getDefault();
          }
          break;
        case EXAMPLE_OR_GENERATED:
          if (propertySchema.getExample() != null) {
            v = propertySchema.getExample();
          } else {
            v = null;
          }
          break;
        default:
          throw new SQLAPIException(format("参数值未设定：%s", var));
      }
    }

    VariableParser.VarType varType =
        VariableParser.VarType.fromTypeFormat(propertySchema.getType(), propertySchema.getFormat());
    if (v == null) {
      // Generated
      switch (varType) {
        case integer:
          v = 0;
          break;
        case string:
          v = "hello world";
          break;
        case number:
          v = 1.0f;
          break;
        case date:
        case datetime:
          v = new Date();
          break;
        case bool:
          v = true;
          break;
        default:
          v = "hello world";
      }
    } else {
      Boolean valid;
      switch (varType) {
        case integer:
          valid = v instanceof Integer;
          break;
        case string:
          valid = v instanceof String;
          break;
        case number:
          valid = v instanceof Number;
          break;
        case date:
          if (v instanceof String) {
            try {
              v = SQLAPIConstants.DATE_FORMAT.parse((String) v);
              valid = true;
              break;
            } catch (ParseException e) {
              valid = false;
              break;
            }
          }
          valid = v instanceof Date;
          break;
        case datetime:
          if (v instanceof String) {
            try {
              v = SQLAPIConstants.DATE_TIME_FORMAT.parse((String) v);
              valid = true;
              break;
            } catch (ParseException e) {
              valid = false;
              break;
            }
          }
          valid = v instanceof Date;
          break;
        case bool:
          valid = v instanceof Boolean;
          break;
        default:
          v = "hello world";
          valid = true;
      }
      if (!valid) {
        throw new SQLAPIException(format("变量值无效: {%s: %s} = %s", var, propertySchema, v));
      }
    }
    return v;
  }
}
