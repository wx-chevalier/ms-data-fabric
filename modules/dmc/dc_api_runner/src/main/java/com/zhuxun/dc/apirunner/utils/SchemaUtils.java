package com.zhuxun.dc.apirunner.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.models.media.Schema;

import static io.swagger.v3.core.util.Json.pretty;

public class SchemaUtils {

  /**
   * 将Schame转换为JSon
   *
   * @param schema
   * @return
   * @throws JsonProcessingException
   */
  public static String convertSchema(Schema schema) throws JsonProcessingException {
    String result = pretty().writeValueAsString(schema).replace("\r", "");
    return result;
  }
}
