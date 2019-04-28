package com.zhuxun.dmc.runner.helpers;

import com.zhuxun.dmc.apim.domain.api.apiimpl.SQLImplDef;
import com.zhuxun.dmc.apim.dto.api.creation.GeneratedAPIModification;
import io.swagger.v3.oas.models.parameters.HeaderParameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class TestData {
  public static String ds_dmc_runner = "dmc_runner";
  public static String PRJ_1_ID = "AA2FC46B-A491-4483-A03F-8597047B7EAE";
  public static String FDL_1_ID = "909ED60C-BDCD-4BFE-A31C-268732785737";
  public static HeaderParameter headerParameter = new HeaderParameter();
  public static GeneratedAPIModification generated_sql_api_creation;

  static {
    headerParameter.setName("lc42-token");
    headerParameter.setDescription("Access token");
  }

  static {
    generated_sql_api_creation =
        new GeneratedAPIModification()
            .setProjectId(PRJ_1_ID)
            .setFolderId(FDL_1_ID)
            .setName("sql generated api")
            .setImpl(
                new SQLImplDef()
                    .setDatasourceName(ds_dmc_runner)
                    .setDialect("mysql")
                    .setSql("SELECT id, api_name AS type FROM dc_api LIMIT {count: integer}"));
  }

  /**
   * 在指定目录中初始化测试用数据源配置
   *
   * @return 初始化的数据源个数
   */
  public static int initDataSourceConfig(File dsConfigDir) throws IOException {
    String dmsAPIConfig =
        "connector.type=mysql"
            + "\nconnection-url="
            + System.getProperty("devDbURL")
            + "\nconnection-user="
            + System.getProperty("devDbUser")
            + "\nconnection-password="
            + System.getProperty("devDbPassword");
    Files.write(
        new File(dsConfigDir, ds_dmc_runner + ".properties").toPath(), dmsAPIConfig.getBytes());
    return 1;
  }
}
