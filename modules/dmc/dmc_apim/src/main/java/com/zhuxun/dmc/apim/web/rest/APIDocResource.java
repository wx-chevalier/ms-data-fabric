package com.zhuxun.dmc.apim.web.rest;

import com.zhuxun.dmc.apim.domain.api.APIDoc;
import com.zhuxun.dmc.apim.dto.api.APIDocTO;
import com.zhuxun.dmc.apim.security.jwt.TokenProvider;
import com.zhuxun.dmc.apim.service.APIDocService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/apidoc")
@Api(description = "接口文档管理相关接口")
public class APIDocResource extends AbstractResource {

  private APIDocService apiDocService;

  @Autowired
  public APIDocResource(TokenProvider tokenProvider,
                        APIDocService apiDocService) {
    super(tokenProvider);
    this.apiDocService = apiDocService;
  }

  public APIDocResource(TokenProvider tokenProvider) {
    super(tokenProvider);
  }

  @ApiOperation(value = "获知指定docId的详情内容", notes = "获取单个文档信息")
  @GetMapping(value = "/{apiDocId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public APIDoc getApiDocById(@PathVariable("apiDocId") String apiDocId) {
    return apiDocService.getDocById(apiDocId);
  }

  @ApiOperation(value = "新增接口文档", notes = "必传参数:文档内容，所属接口ID")
  @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public APIDoc addApiDoc(@RequestBody APIDocTO apiDocTO) {
    return apiDocService.createDoc(currentUserOrThrow().getId(), apiDocTO);
  }

  @ApiOperation(value = "编辑接口文档", notes = "**必传参数** \n  - 文档ID \n - 更新内容字段")
  @PatchMapping(value = "/{apiDocId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public APIDoc updateApiDoc(@PathVariable("apiDocId") String apiDocId,
                             @RequestBody APIDocTO apiDocTO) {
    return apiDocService.updateDocById(
        currentUserOrThrow().getId(), apiDocId, apiDocTO);
  }

  @ApiOperation(value = "逻辑删除接口文档")
  @DeleteMapping(value = "/{apiDocId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public void deleteApiDoc(@PathVariable("apiDocId") String apiDocId) {
    apiDocService.logicDeleteById(apiDocId);
  }
}
