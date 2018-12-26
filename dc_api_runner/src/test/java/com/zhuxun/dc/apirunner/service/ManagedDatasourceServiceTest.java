package com.zhuxun.dc.apirunner.service;

import com.zhuxun.dc.apirunner.exception.ManagedDatasourceException;
import org.junit.Test;

import java.sql.*;

public class ManagedDatasourceServiceTest {

  @Test
  public void getConnection() throws ManagedDatasourceException, SQLException {
    ManagedDatasourceService managedDatasourceService = new ManagedDatasourceService();
    managedDatasourceService.configDir = "/tmp/ds-config";
    Connection conn = managedDatasourceService.getConnection("dc_user");

    // SHOW TABLES
    PreparedStatement stmt = conn.prepareStatement("DESCRIBE dc_action_log");

    System.out.println(conn.getCatalog());

    ResultSet resultSet = stmt.executeQuery();
    ResultSetMetaData metadata = resultSet.getMetaData();
    while (resultSet.next()) {
      for (int i = 1; i <= metadata.getColumnCount(); i++) {
        System.out.println(metadata.getColumnLabel(i) + ": " + resultSet.getObject(i));
      }
      System.out.println("--------------");
    }
  }
}