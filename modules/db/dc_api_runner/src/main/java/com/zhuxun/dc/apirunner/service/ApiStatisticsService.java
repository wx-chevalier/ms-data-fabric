package com.zhuxun.dc.apirunner.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.zhuxun.dc.apirunner.constant.RoleConstant;
import com.zhuxun.dc.apirunner.dao.entity.DcApiStatistics;
import com.zhuxun.dc.apirunner.dao.entity.DcApiStatisticsExample;
import com.zhuxun.dc.apirunner.entity.dto.ApiStatisticsDTO;
import com.zhuxun.dc.apirunner.entity.vo.VAffect;
import com.zhuxun.dc.apirunner.entity.vo.api.VApiStatistics;
import com.zhuxun.dc.apirunner.entity.vo.api.VStatistics;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Api接口统计相关接口数据
 *
 * @author tao
 */
@Service
@Slf4j
public class ApiStatisticsService extends BaseService {

  /**
   * 新增接口执行统计记录
   *
   * @param apiStatistics
   * @return
   */
  @Transactional
  public VAffect addApiStatisticsRecord(DcApiStatistics apiStatistics) {
    int count = dcApiStatisticsMapper.insert(apiStatistics);
    return VAffect.of(count);
  }

  /**
   * 获取某个API的使用列表
   *
   * @param apiStatisticsDTO
   * @return
   */
  @Transactional(readOnly = true)
  public List<VApiStatistics> getApiStatistics(ApiStatisticsDTO apiStatisticsDTO) {
    // TODO 添加分页操作
    DcApiStatisticsExample statisticsExample = apiStatisticsDTO.of();
    statisticsExample.setOrderByClause("access_time DESC");
    List<DcApiStatistics> statistics = dcApiStatisticsMapper.selectByExample(statisticsExample);
    return statistics.stream().map(VApiStatistics::of).collect(Collectors.toList());
  }

  /**
   * 获取管理员(全部)或者企业用户(自己)的按日访问统计信息
   *
   * @return {@link VStatistics}
   */
  @Transactional(readOnly = true)
  @RequiresRoles(
    value = {RoleConstant.ADMIN_NAME, RoleConstant.CLIENT_NAME},
    logical = Logical.OR
  )
  public List<VStatistics> getApiAccessStatisticsWithDay(ApiStatisticsDTO dto) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(dto.getApiId()), "APIID不存在,无法查询!");
    Preconditions.checkArgument(!Strings.isNullOrEmpty(dto.getGroupByFormat()), "分组表达式不存在,无法查询!");
    log.trace("用户{}查询API访问记录,角色信息:{}，分组表达式：{}", getUserId(), getUserRole(), dto.getGroupByFormat());
    List<VStatistics> apiStatisticWithDay;
    if (getUserRole().contains(RoleConstant.CLIENT_NAME)) {
      apiStatisticWithDay = apiManageMapper.getApiAccessStatistics(getUserId(), dto);
    } else {
      apiStatisticWithDay = apiManageMapper.getApiAccessStatistics(null, dto);
    }
    return apiStatisticWithDay;
  }

  /**
   * 获取管理员(全部)的指定API的各个用户访问情况，未指定API将查询各个用户的总访问量
   *
   * @return {@link VStatistics}
   */
  @Transactional(readOnly = true)
  @RequiresRoles(RoleConstant.ADMIN_NAME)
  public List<VStatistics> getApiAccessStatisticsWithUser(String apiId) {
    log.trace("管理员{} 查询API = {} 的用户访问量", getUserId(), apiId);
    List<VStatistics> apiStatisticWithDay = apiManageMapper.getApiAccessStatisticsWithUser(apiId);
    return apiStatisticWithDay;
  }

  @Transactional(readOnly = true)
  @RequiresRoles(value = {RoleConstant.ADMIN_NAME,RoleConstant.CLIENT_NAME},logical = Logical.OR)
  public List<VStatistics> getApiStatisticsWithConsumeTime(String apiId) {
    // TODO 此处时间统计为临时方案，日后重构
    LinkedHashMap<String, Integer> timeStatistics = new LinkedHashMap<String, Integer>(16);
    List<VStatistics> statisticsList;
    if (getUserRole().contains(RoleConstant.CLIENT_NAME)) {
      statisticsList = apiManageMapper.getAPiAccessStatisticsWithConsumeTime(apiId,getUserId());
    }else{
      statisticsList = apiManageMapper.getAPiAccessStatisticsWithConsumeTime(apiId,null);
    }
    statisticsList.forEach(
        consume -> {
          int index = consume.getConsumeTime() / 50;
          String belongTimeStep = String.format("%d ~ %d", index * 50, (index + 1) * 50);
          if (timeStatistics.containsKey(belongTimeStep)) {
            Integer count = timeStatistics.get(belongTimeStep);
            timeStatistics.put(belongTimeStep, count + consume.getCount());
          } else {
            timeStatistics.put(belongTimeStep, consume.getCount());
          }
        });
    statisticsList.clear();

    Iterator<String> iterator = timeStatistics.keySet().iterator();
    while (iterator.hasNext()) {
      String time = iterator.next();
      Integer count = timeStatistics.get(time);
      statisticsList.add(new VStatistics().setTime(time).setCount(count));
    }
    return statisticsList;
  }
}
