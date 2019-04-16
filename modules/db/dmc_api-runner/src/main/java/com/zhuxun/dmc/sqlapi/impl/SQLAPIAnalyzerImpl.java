package com.zhuxun.dmc.sqlapi.impl;

import com.zhuxun.dmc.sqlapi.SQLAPIAnalyzer;
import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import com.zhuxun.dmc.sqlapi.utils.SQLUtils;
import com.zhuxun.dmc.sqlapi.utils.Utils;
import io.swagger.v3.oas.models.media.*;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

import javax.sql.DataSource;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

import static java.util.stream.Collectors.toMap;

@Accessors(chain = true)
@Slf4j
public class SQLAPIAnalyzerImpl implements SQLAPIAnalyzer {
  @Setter @Getter DataSource dataSource;

  @Getter Boolean isQuery;

  @Override
  public Schema analyzeParameter(String sql, String dialect) throws SQLAPIException {
    VariableParser parser = new VariableParser().setSql(sql).parse();
    new SQLValidation().validate(parser.getVarReplacedSql(), dialect);

    this.isQuery = SQLUtils.isQuery(parser.getVarReplacedSql(), dialect);
    this.isQuery = this.isQuery == null ? false : this.isQuery;

    ObjectSchema param = new ObjectSchema();
    param.setProperties(
        parser
            .positionedVariables
            .stream()
            .collect(
                toMap(
                    VariableParser.Var::getName,
                    v -> {
                      if (v.getType() == null) {
                        return new StringSchema();
                      }
                      switch (v.getType()) {
                        case string:
                          return new StringSchema();
                        case integer:
                          return new IntegerSchema();
                        case number:
                          return new NumberSchema();
                        case date:
                          return new DateSchema();
                        case datetime:
                          return new DateTimeSchema();
                        case bool:
                          return new BooleanSchema();
                        default:
                          return new StringSchema();
                      }
                    })));

    return param;
  }

  @Override
  public Schema analyzeResponse(String sql, String dialect, Map<String, Object> paramExample)
      throws SQLAPIException, SQLException {

    VariableParser parser = new VariableParser().setSql(sql).parse();
    Schema paramSchema = analyzeParameter(sql, dialect);

    Map<String, Schema> properties = new HashMap<>();

    if (!SQLUtils.isQuery(parser.getVarReplacedSql(), dialect)) {
      properties.put("affected", new IntegerSchema());
    } else {
      try (Connection connection = this.dataSource.getConnection()) {
        PreparedStatement preparedStmt =
            SQLAPIUtils.prepareStmt(
                connection,
                parser,
                paramSchema,
                paramExample,
                SQLAPIUtils.DefaultValueType.EXAMPLE_OR_GENERATED);

        ResultSet resultSet = preparedStmt.executeQuery();
        ResultSetMetaData metaData = resultSet.getMetaData();
        for (int i = 1; i <= metaData.getColumnCount(); i++) {
          String name = metaData.getColumnLabel(i);
          int columnType = metaData.getColumnType(i);
          properties.put(name, Utils.convertToSchema(columnType));
        }
      }
    }

    ObjectSchema resultSchema = new ObjectSchema();
    resultSchema.setProperties(properties);
    return resultSchema;
  }
}
