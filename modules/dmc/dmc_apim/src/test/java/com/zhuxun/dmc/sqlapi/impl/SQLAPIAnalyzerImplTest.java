package com.zhuxun.dmc.sqlapi.impl;

import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.Test;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;

public class SQLAPIAnalyzerImplTest {
  @Test
  public void test_analyzeParameter_query() throws SQLException, SQLAPIException {
    SQLAPIAnalyzerImpl analyzer = new SQLAPIAnalyzerImpl()
        .setConnection(DriverManager.getConnection("jdbc:mysql://dmc_dev_mysql:3306/dmc_api", "root", ""));
    Schema paramSchema = analyzer.analyzeParameter(
        "SELECT id, api_name FROM dc_api WHERE create_datetime > {createDatetime: datetime}",
        "mysql");
    System.out.println(paramSchema);

    HashMap<String, Object> param = new HashMap<>();
    param.put("createDatetime", new Date());
    Schema respSchema = analyzer.analyzeResponse(
        "SELECT id, api_name FROM dc_api WHERE create_datetime > {createDatetime: datetime}",
        "mysql",
        param);
    System.out.println(respSchema);
  }

  @Test
  public void test_analyzeParameter_insert() throws SQLException, SQLAPIException {
    SQLAPIAnalyzerImpl analyzer = new SQLAPIAnalyzerImpl()
        .setConnection(DriverManager.getConnection("jdbc:mysql://dmc_dev_mysql:3306/dmc_api", "root", ""));
    Schema paramSchema = analyzer.analyzeParameter(
        "INSERT INTO dc_user_api (id, user_id, api_id, create_userid) VALUES({id: string}, {user_id: string}, {api_id: string}, {create_userid: string})",
        "mysql");
    System.out.println(paramSchema);

    HashMap<String, Object> param = new HashMap<>();
    param.put("createDatetime", new Date());
    Schema respSchema = analyzer.analyzeResponse(
        "INSERT INTO dc_user_api (id, user_id, api_id, create_userid) VALUES({id: string}, {user_id: string}, {api_id: string}, {create_userid: string})",
        "mysql",
        param);
    System.out.println(respSchema);
  }

  @Test
  public void test_analyzeParameter_update() throws SQLException, SQLAPIException {
    SQLAPIAnalyzerImpl analyzer = new SQLAPIAnalyzerImpl()
        .setConnection(DriverManager.getConnection("jdbc:mysql://dmc_dev_mysql:3306/dmc_api", "root", ""));
    Schema paramSchema = analyzer.analyzeParameter(
        "UPDATE dc_user_api set create_userid={create_userid} WHERE id={id}",
        "mysql");
    System.out.println(paramSchema);

    HashMap<String, Object> param = new HashMap<>();
    param.put("createDatetime", new Date());
    Schema respSchema = analyzer.analyzeResponse(
        "UPDATE dc_user_api set create_userid={create_userid} WHERE id={id}",
        "mysql",
        param);
    System.out.println(respSchema);
  }
}