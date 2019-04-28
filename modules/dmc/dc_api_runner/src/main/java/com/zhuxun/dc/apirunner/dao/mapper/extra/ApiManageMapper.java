package com.zhuxun.dc.apirunner.dao.mapper.extra;

import com.zhuxun.dc.apirunner.dao.entity.DcEnvironment;
import com.zhuxun.dc.apirunner.entity.dto.ApiStatisticsDTO;
import com.zhuxun.dc.apirunner.entity.vo.VZuulRoute;
import com.zhuxun.dc.apirunner.entity.vo.api.VStatistics;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ApiManageMapper {

  /**
   * 通过apiId获取API的详情信息
   *
   * @param apiId
   * @return
   */
  Map getApiById(@Param("apiId") String apiId);

  /**
   * 获取所有的可用的外部接口
   *
   * @return
   */
  List<VZuulRoute> getAllRoute();

  /**
   * 获取环境中可代理的地址列表
   *
   * @return
   */
  List<DcEnvironment> getProxyNameList();

  /**
   * 获取某个用户的全部访问记录，如果参数UserId 为null，那么将统计全部的数据信息
   *
   * @param userId
   * @return {@link VStatistics}
   */
  List<VStatistics> getApiAccessStatistics(
      @Param("userId") String userId, @Param("condition") ApiStatisticsDTO dto);

  /**
   * 查询指api的各个用户访问量信息，未指定API将查询各个用户的总访问量
   *
   * @return
   */
  List<VStatistics> getApiAccessStatisticsWithUser(@Param("apiId") String apiId);

  /**
   * 查询指定API的访问响应时间
   *
   * @param apiId
   * @return
   */
  List<VStatistics> getAPiAccessStatisticsWithConsumeTime(@Param("apiId") String apiId,@Param("userId")String userId);
}
