package com.zhuxun.dmc.apim.helpers;

import com.zhuxun.dmc.apim.domain.api.param.APIParameter;
import com.zhuxun.dmc.apim.domain.api.param.APIRequestBody;
import com.zhuxun.dmc.apim.dto.api.apiimpl.SQLImpl;
import com.zhuxun.dmc.apim.dto.api.creation.GeneratedAPIModification;
import com.zhuxun.dmc.apim.dto.api.creation.ProxyAPIModification;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.HeaderParameter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;

public class TestData {
  public static String ds_dmc_api = "dmc_api";
  public static String PRJ_1_ID = "AA2FC46B-A491-4483-A03F-8597047B7EAE";
  public static String FDL_1_ID = "909ED60C-BDCD-4BFE-A31C-268732785737";
  public static HeaderParameter headerParameter = new HeaderParameter();

  static {
    headerParameter.setName("lc42-token");
    headerParameter.setDescription("Access token");
  }

  public static ProxyAPIModification proxy_api_creation;
  public static GeneratedAPIModification generated_sql_api_creation;

  static {
    proxy_api_creation = new ProxyAPIModification()
        .setProjectId(PRJ_1_ID)
        .setFolderId(FDL_1_ID)
        .setName("proxy api")
        .setOperation("GET")
        .setProtocol("http")
        .setPath("{{PROXY}}/hello")
        .setParameters(Arrays.asList(
            new APIRequestBody()
                .setSchema(new IntegerSchema()),
            new APIParameter()
                .setParameter(headerParameter)))
        .setResponse(new StringSchema());

    generated_sql_api_creation =
        new GeneratedAPIModification()
            .setProjectId(PRJ_1_ID)
            .setFolderId(FDL_1_ID)
            .setName("sql generated api")
            .setImpl(new SQLImpl()
                .setDatasourceName(ds_dmc_api)
                .setDialect("mysql")
                .setSql("SELECT id, api_name AS type FROM dc_api LIMIT {count: integer}"));
  }

  /**
   * 在指定目录中初始化测试用数据源配置
   *
   * @return 初始化的数据源个数
   */
  public static int initDataSourceConfig(File dsConfigDir) throws IOException {
    String dmsAPIConfig = "connector.type=mysql\n" +
        "connection-url=jdbc:mysql://dmc_dev_mysql:3306/dmc_api\n" +
        "connection-user=root\n" +
        "connection-password=";
    Files.write(
        new File(dsConfigDir, ds_dmc_api + ".properties").toPath(),
        dmsAPIConfig.getBytes());
    return 1;
  }

}
