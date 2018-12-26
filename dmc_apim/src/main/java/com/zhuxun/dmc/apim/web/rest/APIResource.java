package com.zhuxun.dmc.apim.web.rest;

import com.zhuxun.dmc.apim.domain.api.API;
import com.zhuxun.dmc.apim.domain.api.APIDoc;
import com.zhuxun.dmc.apim.domain.sec.User;
import com.zhuxun.dmc.apim.dto.api.APITokenTO;
import com.zhuxun.dmc.apim.dto.api.creation.APIModification;
import com.zhuxun.dmc.apim.dto.token.TokenCreation;
import com.zhuxun.dmc.apim.security.jwt.TokenProvider;
import com.zhuxun.dmc.apim.service.APIDocService;
import com.zhuxun.dmc.apim.service.APIService;
import com.zhuxun.dmc.apim.service.APITokenService;
import com.zhuxun.dmc.apim.service.errors.ServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.models.media.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@Api(description = "接口管理相关接口")
public class APIResource extends AbstractResource {
  private APIService apiService;

  private APIDocService apiDocService;

  private APITokenService apiTokenService;

  @Autowired
  public APIResource(TokenProvider tokenProvider,
                     APITokenService apiTokenService,
                     APIDocService apiDocService,
                     APIService apiService) {
    super(tokenProvider);
    this.apiTokenService = apiTokenService;
    this.apiDocService = apiDocService;
    this.apiService = apiService;
  }

  /**
   * 获取全部的项目类型
   */
  @ApiOperation(value = "新增API接口")
  @PostMapping
  public API createAPI(@RequestBody @ApiParam APIModification apiModification)
      throws ServiceException {

    return apiService.createAPI(currentUserOrThrow().getId(), apiModification);
  }

  /**
   * 逻辑删除接口
   */
  @ApiOperation("逻辑删除接口")
  @DeleteMapping(value = "/{apiId}")
  public void logicDelApi(@PathVariable("apiId") String apiId) {
    apiService.logicDeleteAPI(apiId);
  }

  /**
   * 更新API接口
   */
  @ApiOperation("更新API接口信息")
  @PatchMapping(value = "/{apiId}")
  public API updateApi(@PathVariable("apiId") String apiId,
                       @RequestBody APIModification apiModification)
      throws ServiceException {

    return apiService.updateAPI(currentUserOrThrow().getId(), apiId, apiModification);
  }

  /**
   * 更具 APIID 获取API接口详情
   */
  @ApiOperation(value = "根据ID获取API的接口详情", notes = "暂未完成")
  @GetMapping(value = "/{apiId}")
  public API getApiById(@PathVariable("apiId") String apiId)
      throws ServiceException {

    return apiService.getAPIById(apiId);
  }

  /**
   * 获取指定id的API已授权的用户ID列表
   */
  @ApiOperation(value = "获取指定API的已授权对象", notes = "根据接口ID")
  @GetMapping("/{apiId}/user")
  public List<User> getApiAuthUserById(@PathVariable("apiId") String apiId) {
    return apiService.getAPIAuthUserList(apiId);
  }

  @ApiOperation(value = "获知指定apiId的文档集合", notes = "注意：一个apiId可能对应多个doc")
  @GetMapping(value = "/{apiId}/docs")
  public List<APIDoc> getApiDocByApiId(@PathVariable("apiId") String apiId) {
    return apiDocService.getDocsByAPIId(apiId);
  }

  @PostMapping("/{apiId}/token")
  @ApiOperation(value = "生成测试Token", notes = "生成指定接口的token")
  public APITokenTO createApiAccessToken(@PathVariable("apiId") String apiId,
                                         @RequestBody TokenCreation creation) {
    return new APITokenTO()
        .setToken(apiTokenService.createAPIAccessJWTToken(
            currentUserOrThrow().getId(),
            apiId,
            creation.getEnvId(),
            creation.getClientClaim(),
            creation.getRememberMe()))
        .setApiURL("");
  }

}
