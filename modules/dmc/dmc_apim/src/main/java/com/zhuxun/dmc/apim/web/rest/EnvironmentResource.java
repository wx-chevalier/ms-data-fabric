package com.zhuxun.dmc.apim.web.rest;

import com.zhuxun.dmc.apim.domain.common.Affected;
import com.zhuxun.dmc.apim.domain.environment.Environment;
import com.zhuxun.dmc.apim.dto.environment.EnvironmentDTO;
import com.zhuxun.dmc.apim.security.jwt.TokenProvider;
import com.zhuxun.dmc.apim.service.EnvironmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/environment")
@Api(description = "环境变量相关接口")
public class EnvironmentResource extends AbstractResource {

  EnvironmentService environmentService;

  @Autowired
  public EnvironmentResource(
      TokenProvider tokenProvider,
      EnvironmentService environmentService) {
    super(tokenProvider);
    this.environmentService = environmentService;
  }


  @ApiOperation(value = "新增环境变量信息", notes = "以当前用户身份创建环境变量信息")
  @GetMapping(path = "/{environmentId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Environment addEnvironment(@PathVariable("environmentId") String environmentId) {
    return environmentService.getEnvironmentById(environmentId);
  }

  /**
   * 创建新的环境变量
   *
   * @param environmentDTO
   * @return
   */
  @ApiOperation(value = "新增环境变量信息", notes = "以当前用户身份创建环境变量信息")
  @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Environment addEnvironment(@RequestBody EnvironmentDTO environmentDTO) {
    return environmentService.createEnvironment(currentUserOrThrow().getId(), environmentDTO);
  }


  /**
   * 修改的环境变量
   *
   * @param environmentDTO
   * @return
   */
  @ApiOperation(value = "修改环境变量信息", notes = "以当前用户身份修改环境变量信息 \n - 提供需要更新的参数即可")
  @PatchMapping(value = "/{environmentId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Environment updateEnvironment(@PathVariable("environmentId") String envId,
                                       @RequestBody EnvironmentDTO environmentDTO) {
    return environmentService.updateEnvironmentById(
        currentUserOrThrow().getId(),
        envId,
        environmentDTO);
  }

  /**
   * 删除的环境变量
   *
   * @param environmentIdList
   * @return
   */
  @ApiOperation(value = "批量删除环境变量", notes = "提供环境ID批量删除 \n **注意** 此删除非逻辑删除,而非逻辑删除,返回的结果代表真实删除的数量")
  @DeleteMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public Affected deleteEnvironment(@RequestBody List<String> environmentIdList) {
    return environmentService.logDeleteEnvironment(environmentIdList);
  }
}
