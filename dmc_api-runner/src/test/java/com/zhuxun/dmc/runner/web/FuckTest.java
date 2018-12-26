package com.zhuxun.dmc.runner.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FuckTest {
  @Test
  public void fuck_test() throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
//    String s = "{\"type\":\"object\",\"properties\":{\"count\": {\"type\": \"integer\"}}";
    ObjectSchema objectSchema = new ObjectSchema();
    Map<String, Schema> properties = new HashMap<>();
    properties.put("count", new IntegerSchema());
    objectSchema.setProperties(properties);
    String s0 = objectMapper.writeValueAsString(objectSchema);
    System.out.println(s0);
  }
}
