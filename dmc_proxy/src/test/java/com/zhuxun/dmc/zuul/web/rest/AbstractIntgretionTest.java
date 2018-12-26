package com.zhuxun.dmc.zuul.web.rest;

import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

public abstract class AbstractIntgretionTest {


  protected String adminToken;
  protected String managerToken;
  protected String qy1Token;
  protected String qy2Token;
  protected String qy3Token;

  protected String adminId = "69a9edb6-76f7-486f-8d4f-ce12de973363";
  protected String managerId = "6b81710b-5677-4743-b141-c6510c35593e";
  protected String qy1Id = "6defc1a2-3724-4494-84d2-2ff347a212fd";
  protected String qy2Id = "98545636-db12-4f47-8463-75c847999dea";
  protected String qy3Id = "b8bf3580-a173-43dc-b163-f86829327ae1";

  protected MockMvc mockMvc;



  @Autowired private WebApplicationContext webApplicationContext;

  protected void initTestTokens() {

  }

  protected MockHttpServletRequestBuilder withToken(
      MockHttpServletRequestBuilder builder, String token) {
    if (token == null) {
      return builder;
    } else {
      return builder.header("Authorization", token);
    }
  }

  @Before
  public void setUp() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }
}
