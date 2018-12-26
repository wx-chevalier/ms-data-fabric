package com.zhuxun.dmc.runner.web;

import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.sec.User;
import com.zhuxun.dmc.apim.service.APIService;
import com.zhuxun.dmc.runner.service.APIExecutionService;
import com.zhuxun.dmc.sqlapi.errors.ManagedDatasourceException;
import com.zhuxun.dmc.sqlapi.errors.SQLAPIException;
import com.zhuxun.spring.security.SecurityUtils;
import com.zhuxun.spring.security.jwt.AccessAPIToken;
import com.zhuxun.spring.service.errors.ServiceException;
import com.zhuxun.spring.web.rest.errors.client.NotAuthorizedException;
import com.zhuxun.spring.web.rest.errors.client.NotFoundException;
import com.zhuxun.spring.web.rest.errors.server.ServerErrorException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@Api(description = "接口访问")
@Slf4j
public class APIRunner {
  APIService apiService;

  APIExecutionService apiExecutionService;

  @Autowired
  public APIRunner(APIService apiService, APIExecutionService apiExecutionService) {
    this.apiService = apiService;
    this.apiExecutionService = apiExecutionService;
  }

  @PostMapping("/{apiId}")
  public Object access(@PathVariable("apiId") String apiId, @RequestBody Map<String, Object> param)
      throws ServiceException {
    // FIXME 临时修复管理员测试访问的问题
    API api = apiService.getAPIById(apiId);
    if (api == null) {
      throw new NotFoundException("API not found");
    }
    try {
      return apiExecutionService.execute(api, param);
    } catch (ManagedDatasourceException e) {
      log.warn("数据源错误 {}", apiId, e);
      throw new ServerErrorException("数据源出错");
    } catch (SQLAPIException e) {
      log.warn("接口定义有误 {}", apiId, e);
      throw new ServerErrorException("接口定义有误");
    } catch (SQLException e) {
      log.warn("数据源链接错误 {}", apiId, e);
      throw new ServerErrorException("数据源链接出错");
    }
  }

  private User currentUserOrThrow() {
    return currentUser().orElseThrow(NotAuthorizedException::new);
  }

  private Optional<User> currentUser() {
    Authentication authentication = SecurityUtils.authentication();
    if (authentication instanceof AccessAPIToken) {
      AccessAPIToken jwtAuth = (AccessAPIToken) authentication;
      return Optional.of(new User().setId(jwtAuth.getUserId()).setName(jwtAuth.getName()));
    } else {
      return Optional.empty();
    }
  }
}
