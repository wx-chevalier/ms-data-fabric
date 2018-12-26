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
  public static PreparedStatement prepareStmt(
      Connection connection,
      VariableParser variableParser,
      Schema paramSchema,
      Map<String, Object> paramExample)
      throws SQLException, SQLAPIException {

    PreparedStatement preparedStmt = connection
        .prepareStatement(variableParser.getQuestionMarkedSql());

    int i = 0;
    for (VariableParser.Var var : variableParser.positionedVariables) {
      i++;
      preparedStmt.setObject(i, SQLAPIUtils.getExampleValue(
          var.getName(), (ObjectSchema) paramSchema, paramExample));
    }

    return preparedStmt;
  }

  /**
   * @param schema 假定属性值都为简单类型
   */
  public static Object getExampleValue(
      String var, ObjectSchema schema, Map<String, Object> exampleValues)
      throws SQLAPIException {

    Map<String, Schema> properties = schema.getProperties();
    Schema propertySchema = properties.get(var);
    if (propertySchema == null) {
      throw new SQLAPIException("Unknown var: " + var);
    }

    Object v = exampleValues.getOrDefault(var, null);
    if (v == null) {
      if (propertySchema.getExample() != null) {
        v = propertySchema.getExample();
      }
    }

    VariableParser.VarType varType = VariableParser.VarType.fromTypeFormat(
        propertySchema.getType(),
        propertySchema.getFormat());
    if (v == null) {
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
        throw new SQLAPIException(format(
            "Invalid example value: {%s: %s} = %s",
            var, propertySchema, v));
      }
    }
    return v;
  }
}
