package com.zhuxun.dc.apirunner.sqlapi.impl;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.zhuxun.dc.apirunner.exception.SQLAPIException;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLAPIExecutorImplTest {
  @Test
  public void executeTest() throws SQLException, SQLAPIException {
    SQLAPIExecutorImpl executor = new SQLAPIExecutorImpl();
    executor.cnx(DriverManager.getConnection("jdbc:mysql://localhost:3306/dc_user", "root", null));
    Map<String, Object> param = new HashMap<>();
    param.put("num", 1);

    HashMap<String, Schema> properties = new HashMap<>();
    properties.put("num", new IntegerSchema());

    ObjectSchema paramSchema = new ObjectSchema();
    paramSchema.setProperties(properties);

    Collection<Map<String, Object>> mysql = executor.executeQuery(
        "SELECT username, create_datetime as create_date FROM sec_user LIMIT {num}", "mysql", param, paramSchema);
    System.out.println(mysql);
  }

  @Test
  public void execute_insert_Test() throws SQLException, SQLAPIException {
    SQLAPIExecutorImpl executor = new SQLAPIExecutorImpl();
    executor.cnx(DriverManager.getConnection("jdbc:mysql://localhost:3306/dc_user", "root", null));
    Map<String, Object> param = new HashMap<>();
    param.put("id", 4);
    param.put("role", "hello");

    HashMap<String, Schema> properties = new HashMap<>();
    properties.put("id", new IntegerSchema());
    properties.put("role", new StringSchema());

    ObjectSchema paramSchema = new ObjectSchema();
    paramSchema.setProperties(properties);

    executor
        .rollbackAfterExecute(true)
        .execute(
            "INSERT INTO sec_role (id, role) VALUES ({id}, {role})",
            "mysql",
            param,
            paramSchema);
  }

  @Test
  public void name() {
    List<SQLStatement> stmts = SQLUtils.parseStatements("SELECT * FROM a", "mysql");
    SQLStatement stmt = stmts.get(0);
    System.out.println(stmt instanceof SQLSelectStatement);
  }
}