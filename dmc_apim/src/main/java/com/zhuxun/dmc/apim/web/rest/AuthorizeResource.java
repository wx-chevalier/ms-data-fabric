package com.zhuxun.dmc.apim.web.rest;

import com.zhuxun.dmc.apim.domain.common.Affected;
import com.zhuxun.dmc.apim.dto.AuthorizeResourceTO;
import com.zhuxun.dmc.apim.security.jwt.TokenProvider;
import com.zhuxun.dmc.apim.service.APIService;
import com.zhuxun.dmc.apim.service.AuthorizationService;
import com.zhuxun.dmc.apim.web.rest.errors.client.NotFoundException;
import com.zhuxun.dmc.apim.web.rest.errors.server.ServiceUnavailableException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

import static java.lang.String.format;

@RestController
@RequestMapping(value = "/auth")
@Api(description = "接口授权、数据源授权相关接口")
public class AuthorizeResource extends AbstractResource {
  private APIService apiService;
  private AuthorizationService authorizationService;

  @Autowired
  public AuthorizeResource(TokenProvider tokenProvider,
                           APIService apiService,
                           AuthorizationService authorizationService) {
    super(tokenProvider);
    this.apiService = apiService;
    this.authorizationService = authorizationService;
  }

  /**
   * 批量新增授权API信息
   */
  @ApiOperation(value = "将API授权于多用户", notes = "提供一个api的主键和用户(id)集合，接口将将此接口授权与集合中的多个用户")
  @PostMapping(value = "/api/{apiId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public void authorizeAPI(@PathVariable("apiId") String apiId,
                           @ApiParam("被授权的用户列表") @RequestBody Set<String> userIdSet) {
    if (apiService.exists(apiId)) {
      authorizationService.authorizeAPI(currentUserOrThrow().getId(), apiId, userIdSet);
    } else {
      throw new NotFoundException(format("API{id=%s} not found", apiId));
    }
  }

  /**
   * 批量移除授权API信息
   */
  @ApiOperation(value = "指定移除某个API的授权用户", notes = "提供一个api的主键和用户(id)集合，接口将删除此接口的授权用户")
  @DeleteMapping(value = "/api/{apiId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public void deauthorizeAPI(@PathVariable("apiId") String apiId,
                             @ApiParam("要移除授权的用户列表") @RequestBody Set<String> userIdSet) {
    if (apiService.exists(apiId)) {
      authorizationService.deauthAPI(currentUserOrThrow().getId(), apiId, userIdSet);
    } else {
      throw new NotFoundException(format("API{id=%s} not found", apiId));
    }
  }


  /**
   * 删除授权数据源
   */
  @ApiOperation("批量删除数据源授权")
  @DeleteMapping(value = "/ds", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Affected delDataSourceAuth(@RequestBody Set<String> resId) {
    throw new ServiceUnavailableException("Not implemented");
  }

  /**
   * 批量新增授权数据源信息
   */
  @ApiOperation("批量新增数据源授权")
  @PostMapping(value = "/ds", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Affected addAuthDataSource(@RequestBody AuthorizeResourceTO authorize) {
    throw new ServiceUnavailableException("Not implemented");
  }
}
