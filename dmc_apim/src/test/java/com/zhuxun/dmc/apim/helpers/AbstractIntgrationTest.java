package com.zhuxun.dmc.apim.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.zhuxun.dmc.apim.Application;
import com.zhuxun.dmc.apim.config.UserConstants;
import com.zhuxun.dmc.apim.config.properties.ApplicationProperties;
import com.zhuxun.dmc.apim.security.jwt.JWTAuthenticationToken;
import com.zhuxun.dmc.apim.security.jwt.TokenProvider;
import lombok.Getter;
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
import org.springframework.web.context.WebApplicationContext;

import java.util.Collection;
import java.util.stream.Collectors;

import static com.zhuxun.dmc.apim.helpers.TestData.initDataSourceConfig;
import static com.zhuxun.dmc.apim.security.jwt.JWTFilter.BEARER;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
@WebAppConfiguration
public abstract class AbstractIntgrationTest {
  @Rule
  public TemporaryFolder dsConfigDir = new TemporaryFolder();

  public Integer managedDatabaseCount = 0;

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

  protected void initTestTokens() {
    this.adminToken = getToken(adminId, "admin", ImmutableList.of("ROLE_" + UserConstants.ROLE_ADMIN));
    this.managerToken = getToken(managerId, "manager", ImmutableList.of("ROLE_" + UserConstants.ROLE_MANAGER));
    this.qy1Token = getToken(qy1Id, "qy1", ImmutableList.of("ROLE_" + UserConstants.ROLE_CLIENT));
    this.qy2Token = getToken(qy2Id, "qy2", ImmutableList.of("ROLE_" + UserConstants.ROLE_CLIENT));
    this.qy3Token = getToken(qy3Id, "qy3", ImmutableList.of("ROLE_" + UserConstants.ROLE_CLIENT));
  }

  protected String getToken(String userId, String username, Collection<String> authorities) {
    return BEARER + " " + getTokenProvider().createToken(
        new JWTAuthenticationToken(
            userId, username,
            authorities.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())),
        false);
  }

  protected MockMvc mockMvc;

  @Autowired
  protected ObjectMapper objectMapper;

  @Getter
  @Autowired
  private TokenProvider tokenProvider;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Autowired
  private FilterChainProxy filterChainProxy;

  @Autowired
  private ApplicationProperties applicationProperties;

  @Before
  public void setUp() throws Exception {
    this.mockMvc = webAppContextSetup(webApplicationContext)
        .addFilters(filterChainProxy)
        .build();
    initTestTokens();
    this.managedDatabaseCount = initDataSourceConfig(dsConfigDir.getRoot());

    applicationProperties.getManagedDatasource()
        .setDsConfigDir(dsConfigDir.getRoot().toString());

    ResourceTestHelpers.setMockMvc(mockMvc);
    ResourceTestHelpers.setObjectMapper(objectMapper);
  }
}
