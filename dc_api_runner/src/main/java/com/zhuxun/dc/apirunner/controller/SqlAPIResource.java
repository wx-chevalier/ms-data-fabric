package com.zhuxun.dc.apirunner.controller;

import com.zhuxun.dc.apirunner.dao.entity.DcApi;
import com.zhuxun.dc.apirunner.entity.dto.SqlApiCreation;
import com.zhuxun.dc.apirunner.entity.vo.api.VApi;
import com.zhuxun.dc.apirunner.entity.vo.api.VApiImpl;
import com.zhuxun.dc.apirunner.entity.vo.api.VParam;
import com.zhuxun.dc.apirunner.entity.vo.api.VResponse;
import com.zhuxun.dc.apirunner.exception.ManagedDatasourceException;
import com.zhuxun.dc.apirunner.exception.SQLAPIException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping(value = "/sqlapi")
@Api(description = "保存测试的数据信息")
public class SqlAPIResource extends BaseResource {

  /**
   * 提供数据创建新的接口类型
   */
  @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "提供JSON格式化数据保存此接口至数据库")
  public VApi createSqlApi(@RequestBody SqlApiCreation sqlApiCreation)
      throws SQLException, IOException, SQLAPIException, ManagedDatasourceException {
    String apiId = sqlApiSaveService.createSqlApi(sqlApiCreation);
    DcApi api = sqlApiSaveService.getApiById(apiId);
    List<VParam> paramList = sqlApiSaveService.getApiParamByapiId(apiId);
    VResponse apiResponse = sqlApiSaveService.getApiResponseByapiId(apiId);
    VApiImpl apiImpl = sqlApiSaveService.getApiImplByapiId(apiId);
    return VApi.of(api, paramList, apiResponse, apiImpl);
  }
}
