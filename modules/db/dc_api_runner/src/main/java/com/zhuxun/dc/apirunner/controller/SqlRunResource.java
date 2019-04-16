package com.zhuxun.dc.apirunner.controller;

import com.zhuxun.dc.apirunner.constant.HttpConstant;
import com.zhuxun.dc.apirunner.dao.entity.DcApiStatistics;
import com.zhuxun.dc.apirunner.entity.vo.VToken;
import com.zhuxun.dc.apirunner.exception.ManagedDatasourceException;
import com.zhuxun.dc.apirunner.exception.SQLAPIException;
import com.zhuxun.dc.apirunner.exception.SchemaValidationError;
import com.zhuxun.dc.apirunner.sqlapi.SQLValidationError;
import com.zhuxun.dc.apirunner.utils.UUIDUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping(value = "/api")
@Api(description = "保存测试的数据信息")
public class SqlRunResource extends BaseResource {

  /**
   * 运行接口信息
   *
   * @param apiId
   * @param param
   * @return
   * @throws SQLException
   * @throws SQLAPIException
   */
  @PostMapping(value = "/{apiId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  @ApiOperation(value = "运行指定的接口")
  public Object addSqlImple(
      @PathVariable("apiId") String apiId,
      @RequestBody Map<String, Object> param,
      @RequestHeader(HttpConstant.TURELORE_TOKEN) String token,
      HttpServletRequest request)
      throws Exception {
    // 计算执行时间
    long startTime = System.currentTimeMillis();
    // 解析Token
    VToken vToken = sqlApiRunService.checkTokenArgument(apiId, token);
    // TODO 此处添加SQL统计信息
    DcApiStatistics statistics = new DcApiStatistics();
    statistics.setId(UUIDUtils.getUUID());
    statistics.setAccessTime(new Date());
    statistics.setProxyMethod(HttpMethod.POST.name());
    statistics.setProxyPath("NO_PROXY_PATH");
    statistics.setApiPath(request.getRequestURL().toString());
    statistics.setApiId(apiId);
    statistics.setUserId(vToken.getUserId());
    // 保存异常信息
    Exception exception = null;
    Object dataSetObject = null;
    try {
      dataSetObject = sqlApiRunService.runSqlImpl(apiId, param, token);
    } catch (Exception ex) {
      // 保存异常信息,在完成统计信息之后在继续抛出
      exception = ex;
    }
    long consumeTime = System.currentTimeMillis() - startTime;
    // 根据是否存在异常以及异常的类型来记录不同的响应状态
    if (exception != null) {
      int statusCode = 500;
      if (exception instanceof ManagedDatasourceException) {
        statusCode = HttpStatus.INTERNAL_SERVER_ERROR.value();
      } else if (exception instanceof SchemaValidationError) {
        statusCode = HttpStatus.BAD_REQUEST.value();
      } else if (exception instanceof SQLValidationError) {
        statusCode = HttpStatus.UNPROCESSABLE_ENTITY.value();
      }
      statistics.setStatusCode(statusCode);
    } else {
      statistics.setStatusCode(HttpStatus.OK.value());
    }
    statistics.setConsumeTime((int) consumeTime);
    apiStatisticsService.addApiStatisticsRecord(statistics);
    // 如果存在异常则抛出，不存在异常则返回SQL执行结果
    if (exception != null) {
      throw exception;
    }
    return dataSetObject;
  }
}
