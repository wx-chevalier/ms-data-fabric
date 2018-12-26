package com.zhuxun.dc.apirunner.service.datasource.metadata;

import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLMetadataRetreiverTest {
  @Test
  public void testUsage() throws SQLException, ClassNotFoundException {
    String driver = "com.mysql.cj.jdbc.Driver";
    String url = "jdbc:mysql://localhost:3306";
    String username = "root";
    String password = "";

    Class.forName(driver);
    Connection conn = DriverManager.getConnection(url, username, password);

    MySQLMetadataRetreiver retreiver = new MySQLMetadataRetreiver();
    System.out.println(retreiver.getCatalogs(conn));
    System.out.println(retreiver.getSchemaNames(conn, null));
    System.out.println(retreiver.getTables(conn, null, "dmc_user"));
    System.out.println(retreiver.getColumns(conn, null, "dmc_user", "sec_user"));
  }
}