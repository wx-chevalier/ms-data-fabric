package com.zhuxun.dc.apirunner.sqlapi.impl;

import com.zhuxun.dc.apirunner.exception.SQLAPIException;
import com.zhuxun.dc.apirunner.sqlapi.SQLValidationError;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLAPIAnalyzerImplTest {
  @Test(expected = SQLValidationError.class)
  public void analyzeParameter_validation_1_Test() throws SQLAPIException, SQLException, SQLAPIException {
    // 不允许 select *
    Schema schema = new SQLAPIAnalyzerImpl().analyzeParameter(
        "SELECT * FROM a WHERE id={fuck}", "mysql");
    Json.prettyPrint(schema);
  }

  @Test
  public void analyzeResponseTest() throws SQLAPIException, SQLException {
    Schema schema = new SQLAPIAnalyzerImpl()
        .cnx(DriverManager.getConnection("jdbc:mysql://localhost:3306/dc_user", "root", null))
        .analyzeResponse("SELECT username FROM sec_user WHERE id={fuck}", "mysql");
    Json.prettyPrint(schema);
  }

  @Test
  public void name() throws SQLException {
    Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/dc_user", "root", null);
    PreparedStatement stmt = connection.prepareStatement("INSERT INTO sec_role (id, role) VALUES (?, ?)");
    stmt.setObject(1, 4);
    stmt.setObject(2, "fucked");
    System.out.println(stmt.executeUpdate());
  }
}