package com.zhuxun.dmc.apim.service.impl.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class APIServiceImplTest {
  private ObjectMapper objectMapper = new ObjectMapper();

  @Test
  public void test_Schema_serialization_deserialization() throws IOException {
    StringSchema schema1 = new StringSchema();

    String s = objectMapper.writeValueAsString(schema1);
    System.out.println(s);

    Schema schema2 = objectMapper.readValue(s, Schema.class);

    assertEquals(schema1.getType(), schema2.getType());
    assertEquals(schema1.getFormat(), schema2.getFormat());
    assertEquals(schema1.getName(), schema2.getName());
  }
}