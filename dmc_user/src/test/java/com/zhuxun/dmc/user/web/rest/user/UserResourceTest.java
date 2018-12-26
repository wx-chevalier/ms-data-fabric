package com.zhuxun.dmc.user.web.rest.user;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhuxun.dmc.user.Application;
import com.zhuxun.dmc.user.config.UserConstants;
import com.zhuxun.dmc.user.domain.sec.Role;
import com.zhuxun.dmc.user.domain.sec.User;
import com.zhuxun.dmc.user.dto.user.UserCreation;
import com.zhuxun.dmc.user.dto.user.UserUpdate;
import com.zhuxun.dmc.user.security.jwt.TokenProvider;
import com.zhuxun.dmc.user.web.rest.AbstractIntgretionTest;
import lombok.Getter;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.util.AssertionErrors.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@Sql(scripts = {"classpath:db/test/cleanup_test_users.sql"},
    executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = {"classpath:db/test/test_users.sql"})
public class UserResourceTest extends AbstractIntgretionTest {
  private MockMvc mockMvc;

  @Getter
  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private FilterChainProxy filterChainProxy;

  @Before
  public void setUp() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext)
        .addFilters(filterChainProxy)
        .build();
    initTestTokens();
  }

  @Test
  public void test_admin_getUserList() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(
        withToken(get("/user"), adminToken))
        .andExpect(status().isOk())
        .andReturn();
    List<User> userList = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        new TypeReference<List<User>>() {
        });
    assertNotEquals(0, userList.size());
  }

  @Test
  public void test_manager_getUserList() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(
        withToken(get("/user"), managerToken))
        .andExpect(status().isOk())
        .andReturn();
    List<User> userList = objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        new TypeReference<List<User>>() {
        });
    assertNotEquals(0, userList.size());
  }

  @Test
  public void test_client_getUserList() throws Exception {
    this.mockMvc.perform(withToken(get("/user"), qy1Token))
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void test_admin_createUser() throws Exception {
    User user = serialize_user_result(
        post_user("lotuc", "pass", adminToken).andReturn());
    assertEquals("lotuc", user.getName());
  }

  @Test
  public void test_manager_or_client_createUser() throws Exception {
    post_user("lotuc", "pass", managerToken)
        .andExpect(status().is4xxClientError());
    post_user("lotuc", "pass", qy1Token)
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void test_admin_getUser() throws Exception {
    get_user(adminId, adminToken)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("admin")));
    get_user(managerId, adminToken)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("manager")));
    get_user(qy1Id, adminToken)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("qy1")));
  }

  @Test
  public void test_manager_getUser_get_admin() throws Exception {
    get_user(adminId, managerToken)
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void test_manager_getUser_get_clients() throws Exception {
    get_user(qy1Id, managerToken)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("qy1")));
    get_user(qy2Id, managerToken)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("qy2")));
  }

  @Test
  public void test_manager_getUser_get_self() throws Exception {
    get_user(managerId, managerToken)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("manager")));
  }

  @Test
  public void test_client_getUser_get_self() throws Exception {
    get_user(qy1Id, qy1Token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("qy1")));
    get_user(qy2Id, qy2Token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("qy2")));
  }

  @Test
  public void test_client_getUser_get_others() throws Exception {
    get_user(qy2Id, qy1Token)
        .andExpect(status().is4xxClientError());
  }

  @Test
  public void test_admin_removeUser() throws Exception {
    get_user(qy1Id, adminToken).andExpect(status().isOk());
    delete_user(qy1Id, adminToken).andExpect(status().isOk());
    get_user(qy1Id, adminToken).andExpect(status().is4xxClientError());
  }

  @Test
  public void test_client_removeUser() throws Exception {
    get_user(qy2Id, adminToken).andExpect(status().isOk());
    delete_user(qy2Id, qy1Token).andExpect(status().is4xxClientError());
  }

  @Test
  public void test_manager_removeUser() throws Exception {
    get_user(qy2Id, adminToken).andExpect(status().isOk());
    delete_user(qy2Id, managerToken).andExpect(status().is4xxClientError());
  }

  @Test
  public void test_admin_updateUser() throws Exception {
    get_user(qy1Id, adminToken)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("qy1")));
    update_user(qy1Id, "hello", null, adminToken);
    get_user(qy1Id, adminToken)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("hello")));

    get_user(managerId, adminToken)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("manager")));
    update_user(managerId, "hello", null, adminToken);
    get_user(managerId, adminToken)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("hello")));
  }

  @Test
  public void test_non_admin_updateUser_update_others() throws Exception {
    update_user(qy2Id, "damn", null, qy1Token).andExpect(status().is4xxClientError());
    update_user(qy2Id, "damn", null, managerToken).andExpect(status().is4xxClientError());
  }

  @Test
  public void test_client_updateUser_update_self() throws Exception {
    update_user(qy1Id, "damn", null, qy1Token).andExpect(status().isOk());
    get_user(qy1Id, qy1Token)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("damn")));
  }

  @Test
  public void test_manager_updateUser_update_self() throws Exception {
    update_user(managerId, "fuckingshit", null, managerToken).andExpect(status().isOk());
    get_user(managerId, managerToken)
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name", is("fuckingshit")));
  }

  @Test
  public void test_admin_getClientList() throws Exception {
    assertAllClients(serialize_user_list_result(
        get_clients(adminToken)
            .andExpect(status().isOk())
            .andReturn()));
  }

  @Test
  public void test_manager_getClientList() throws Exception {
    assertAllClients(serialize_user_list_result(
        get_clients(managerToken)
            .andExpect(status().isOk())
            .andReturn()));
  }

  @Test
  public void test_client_getClientList() throws Exception {
    get_clients(qy1Token).andExpect(status().is4xxClientError());
  }

  private void assertAllClients(List<User> userList) throws Exception {
    for (User usr : userList) {
      User u = serialize_user_result(get_user(usr.getId(), adminToken).andReturn());
      String errMsg = u + " does not contain role CLIENT";
      assertNotNull(errMsg, u.getRoles());
      assertTrue(errMsg, u.getRoles().size() > 0);
      Boolean containsClientRole = false;
      for (Role r : u.getRoles()) {
        if (UserConstants.ROLE_CLIENT.equals(r.getName())) {
          containsClientRole = true;
          break;
        }
      }
      assertTrue(errMsg, containsClientRole);
    }
  }

  private ResultActions get_clients(String token) throws Exception {
    return this.mockMvc.perform(withToken(get("/user/client"), token));
  }

  private ResultActions post_user(String username, String password, String token) throws Exception {
    return this.mockMvc.perform(Optional.of(post("/user"))
        .map(b -> withToken(b, token)).get()
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(
            new UserCreation().setName(username).setPassword(password))));
  }

  private ResultActions get_user(String userId, String token) throws Exception {
    return this.mockMvc.perform(withToken(get("/user/{userId}", userId), token));
  }

  private ResultActions delete_user(String userId, String token) throws Exception {
    return this.mockMvc.perform(withToken(delete("/user/{userId}", userId), token));
  }

  private ResultActions update_user(String userId, String username, String password, String token) throws Exception {
    return this.mockMvc.perform(withToken(patch("/user/{userId}", userId), token)
        .contentType(MediaType.APPLICATION_JSON_UTF8)
        .content(objectMapper.writeValueAsString(
            Optional.of(new UserUpdate())
                .map(update -> {
                  Optional.ofNullable(username).ifPresent(update::setName);
                  Optional.ofNullable(password).ifPresent(update::setPassword);
                  return update;
                }).get())));
  }

  private User serialize_user_result(MvcResult mvcResult) throws IOException {
    return objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        User.class);
  }

  private List<User> serialize_user_list_result(MvcResult mvcResult) throws IOException {
    return objectMapper.readValue(
        mvcResult.getResponse().getContentAsString(),
        new TypeReference<List<User>>() {
        });
  }

}