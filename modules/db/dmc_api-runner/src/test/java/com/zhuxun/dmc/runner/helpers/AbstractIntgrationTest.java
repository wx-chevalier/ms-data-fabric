package com.zhuxun.dmc.runner.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.zhuxun.dmc.runner.Application;
import com.zhuxun.dmc.runner.config.ApplicationProperties;
import com.zhuxun.dmc.user.config.UserConstants;
import com.zhuxun.spring.security.jwt.AccessAPIToken;
import com.zhuxun.spring.security.jwt.TokenProvider;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.zhuxun.dmc.runner.helpers.TestData.initDataSourceConfig;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
@Slf4j
public abstract class AbstractIntgrationTest {
  @Rule public TemporaryFolder dsConfigDir = new TemporaryFolder();

  public Integer managedDatabaseCount = 0;

  protected String accountToken;
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
  @Autowired protected ObjectMapper objectMapper;
  @Getter @Autowired private TokenProvider tokenProvider;
  @Autowired private WebApplicationContext webApplicationContext;
  @Autowired private FilterChainProxy filterChainProxy;
  @Autowired private ApplicationProperties applicationProperties;

  protected void initTestTokens() {
    this.accountToken =
        getToken(adminId, "account", ImmutableList.of("ROLE_" + UserConstants.ROLE_ACCOUNT));
    this.managerToken =
        getToken(managerId, "manager", ImmutableList.of("ROLE_" + UserConstants.ROLE_MANAGER));
    this.qy1Token = getToken(qy1Id, "qy1", ImmutableList.of("ROLE_" + UserConstants.ROLE_CLIENT));
    this.qy2Token = getToken(qy2Id, "qy2", ImmutableList.of("ROLE_" + UserConstants.ROLE_CLIENT));
    this.qy3Token = getToken(qy3Id, "qy3", ImmutableList.of("ROLE_" + UserConstants.ROLE_CLIENT));
  }

  protected MockHttpServletRequestBuilder withToken(
      MockHttpServletRequestBuilder builder, String token) {
    if (token == null) {
      return builder;
    } else {
      return builder.header("Authorization", token);
    }
  }

  protected String getToken(String userId, String envId, Collection<String> authorities) {
    String token =
        getTokenProvider()
            .createToken(
                new AccessAPIToken(
                    userId,
                    envId,
                    authorities
                        .stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList())),
                false);
    log.info("Generated token: {} {} {}: {}", userId, envId, authorities, token);
    return token;
  }

  @Before
  public void setUp() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext).addFilters(filterChainProxy).build();
    initTestTokens();
    this.managedDatabaseCount = initDataSourceConfig(dsConfigDir.getRoot());

    applicationProperties.getManagedDatasource().setDsConfigDir(dsConfigDir.getRoot().toString());

    ResourceTestHelpers.setMockMvc(mockMvc);
    ResourceTestHelpers.setObjectMapper(objectMapper);
  }
}
