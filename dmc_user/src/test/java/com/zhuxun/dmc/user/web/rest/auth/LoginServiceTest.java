package com.zhuxun.dmc.user.web.rest.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.user.Application;
import com.zhuxun.dmc.user.domain.auth.BearerToken;
import com.zhuxun.dmc.user.dto.user.UserLogin;
import com.zhuxun.dmc.user.security.jwt.TokenProvider;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@Sql(scripts = {"classpath:db/test/cleanup_test_users.sql"},
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:db/test/test_users.sql"})
public class LoginServiceTest {
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void setUp() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).build();
  }

  @Test
  public void test_successfully_login() throws Exception {
    assertNotNull(mockMvc);
    MvcResult mvcResult = this.mockMvc.perform(post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(new UserLogin()
            .setUsername("admin").setPassword("123"))))
        .andExpect(result ->
            assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus()))
        .andReturn();

    BearerToken token = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(), BearerToken.class);
    assertNotNull(token);
    assertNotNull(token.getToken());
    assertTrue(tokenProvider.validateToken(token.getToken()));
  }

  @Test
  public void test_failed_login() throws Exception {
    this.mockMvc.perform(post("/auth/login")
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(new UserLogin()
            .setUsername("admin").setPassword("123fjdskla"))))
        .andExpect(result -> assertEquals(
            HttpStatus.UNAUTHORIZED.value(),
            result.getResponse().getStatus()));
  }
}