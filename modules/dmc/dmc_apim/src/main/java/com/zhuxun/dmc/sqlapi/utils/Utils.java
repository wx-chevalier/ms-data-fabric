package com.zhuxun.dmc.sqlapi.utils;

import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import io.swagger.v3.oas.models.media.*;

import java.sql.Types;

public class Utils {
  /**
   * @param sqlType java.sql.Types
   */
  public static Schema convertToSchema(int sqlType) throws SQLAPIException {
    switch (sqlType) {
      case Types.BIT:
      case Types.TINYINT:
      case Types.SMALLINT:
      case Types.INTEGER:
      case Types.BIGINT:
        return new IntegerSchema();
      case Types.FLOAT:
      case Types.REAL:
      case Types.DOUBLE:
      case Types.NUMERIC:
      case Types.DECIMAL:
        return new NumberSchema();
      case Types.CHAR:
      case Types.VARCHAR:
      case Types.LONGVARCHAR:
        return new StringSchema();
      case Types.TIMESTAMP:
      case Types.DATE:
        return new DateSchema();
      case Types.BOOLEAN:
        return new BooleanSchema();
      case Types.TIME:
        break;
      case Types.BINARY:
        break;
      case Types.VARBINARY:
        break;
      case Types.LONGVARBINARY:
        break;
      case Types.NULL:
        break;
      case Types.OTHER:
        break;
      case Types.JAVA_OBJECT:
        break;
      case Types.DISTINCT:
        break;
      case Types.STRUCT:
        break;
      case Types.ARRAY:
        break;
      case Types.BLOB:
        break;
      case Types.CLOB:
        break;
      case Types.REF:
        break;
      case Types.DATALINK:
        break;
      case Types.ROWID:
        break;
      case Types.NCHAR:
        break;
      case Types.NVARCHAR:
        break;
      case Types.LONGNVARCHAR:
        break;
      case Types.NCLOB:
        break;
      case Types.SQLXML:
        break;
      case Types.REF_CURSOR:
        break;
      case Types.TIME_WITH_TIMEZONE:
        break;
      case Types.TIMESTAMP_WITH_TIMEZONE:
        break;
    }
    throw new SQLAPIException("Result type not supported: " + sqlType);
  }

}
