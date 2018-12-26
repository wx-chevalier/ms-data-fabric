package com.zhuxun.dmc.apim.web.rest;

import com.zhuxun.dmc.apim.domain.common.Affected;
import com.zhuxun.dmc.apim.domain.model.ModelDefinition;
import com.zhuxun.dmc.apim.dto.model.ModelDefinitionCreation;
import com.zhuxun.dmc.apim.security.jwt.TokenProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/model")
@Api(description = "模型定义相关接口")
public class ModelDefinitionResources extends AbstractResource {

  public ModelDefinitionResources(TokenProvider tokenProvider) {
    super(tokenProvider);
  }

  @GetMapping(value = "/{modelId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "通过ModelID获取模型接口")
  public ModelDefinition getModelById(@PathVariable("modelId") String modelId) {
    return null;
  }


  @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "新增模型接口", notes = "返回新增后的数据信息")
  public ModelDefinition addModel(@RequestBody ModelDefinitionCreation modelDefinitionCreation) {
    return null;
  }

  @DeleteMapping(value = "/{modelId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "删除模型", notes = "非逻辑删除，返回影响行数")
  public Affected deleteModel(@PathVariable("modelId") String modelId) {
    return null;
  }

  @PatchMapping(value = "/{modelId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "更新模型", notes = "返回更新后的模型数据")
  public ModelDefinition updateModelById(@PathVariable("modelId") String modelId, @RequestBody ModelDefinitionCreation modelDefinitionCreation) {
    return null;
  }
}
