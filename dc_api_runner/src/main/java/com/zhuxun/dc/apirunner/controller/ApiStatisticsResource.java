package com.zhuxun.dc.apirunner.controller;

import com.zhuxun.dc.apirunner.entity.dto.ApiStatisticsDTO;
import com.zhuxun.dc.apirunner.entity.vo.api.VStatistics;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * API接口访问统计相关接口
 *
 * @author tao
 */

@RestController
@RequestMapping(value = "/apistatistics")
@Api(description = "API访问记录统计相关接口")
public class ApiStatisticsResource extends BaseResource {

  @PostMapping("/time")
  @ApiOperation(value = "按天统计API的访问量",notes = "查询当前登录用户的访问记录 \n + admin:查询全部的记录 \n + client: 查询其账户的访问记录 \n + 必填参数: 分组表达式请参考[](http://www.w3school.com.cn/sql/func_date_format.asp)")
  public List<VStatistics> getApiStatisticWithDay(@RequestBody  ApiStatisticsDTO dto) {
    List<VStatistics> statistics = apiStatisticsService.getApiAccessStatisticsWithDay(dto);
    return statistics;
  }

  @PostMapping("/user")
  @ApiOperation(value = "按用户统计API的访问量",notes = "返回用户的ID信息,非法用户使用NO TOKEN标记,未指定api将查询全部的api统计记录")
  public List<VStatistics> getApiStatisticsWithUser(String apiId){
    List<VStatistics> statistics = apiStatisticsService.getApiAccessStatisticsWithUser(apiId);
    return statistics;
  }


  @PostMapping("/api")
  @ApiOperation(value = "统计指定API的各次的请求时间统计",notes = "此方法属于临时方案，后期需要重构")
  public List<VStatistics> getAccessStatisticsWithConsumeTime(String apiId){
    List<VStatistics> statistics = apiStatisticsService.getApiStatisticsWithConsumeTime(apiId);
    return statistics;
  }
}
