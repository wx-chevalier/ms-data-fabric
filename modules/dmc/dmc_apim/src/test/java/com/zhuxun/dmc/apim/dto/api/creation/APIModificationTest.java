package com.zhuxun.dmc.apim.dto.api.creation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.api.param.APIParam;
import com.zhuxun.dmc.apim.domain.api.param.APIRequestBody;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class APIModificationTest {
  private ObjectMapper objectMapper;

  @Before
  public void setUp() throws Exception {
    this.objectMapper = new ObjectMapper();
  }

  @Test
  public void test_serialization() throws IOException {
    ProxyAPIModification pm0 = new ProxyAPIModification().setParameters(new ArrayList<>());
    pm0.getParameters().add(new APIRequestBody());
    String s0 = objectMapper.writeValueAsString(pm0);

    System.out.println(s0);
    APIModification pm0_0 = objectMapper.readValue(s0, APIModification.class);
    assertTrue(pm0_0 instanceof ProxyAPIModification);

    ProxyAPIModification pm0_1 = (ProxyAPIModification) pm0_0;
    assertEquals(API.APIType.PROXY, pm0_1.getType());
  }

  @Test
  public void test_deserialization() throws IOException {
    String s0 = "{\n" +
        "  \"type\": \"PROXY\",\n" +
        "  \"type\": \"proxy api\",\n" +
        "  \"operation\": \"GET\",\n" +
        "  \"protocol\": \"http\",\n" +
        "  \"path\": \"{{PROXY}}/hello/world\",\n" +
        "  \"response\": { \"type\": \"string\" },\n" +
        "  \"summary\": \"\",\n" +
        "  \"parameters\": [{ \"type\": \"REQUEST_BODY\", \"schema\": { \"type\": \"string\" } }]\n" +
        "}";

    APIModification m0 = objectMapper.readValue(s0, APIModification.class);
    assertTrue(m0 instanceof ProxyAPIModification);
    ProxyAPIModification pm0 = (ProxyAPIModification) m0;
    assertEquals(API.APIType.PROXY, pm0.getType());
    assertEquals(1, pm0.getParameters().size());
    assertTrue(pm0.getParameters().get(0) instanceof APIRequestBody);
    assertEquals(APIParam.ParamType.REQUEST_BODY, pm0.getParameters().get(0).getType());
  }
}