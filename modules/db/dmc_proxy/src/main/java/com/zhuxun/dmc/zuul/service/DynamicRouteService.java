package com.zhuxun.dmc.zuul.service;

import com.google.common.base.Preconditions;
import com.zhuxun.dmc.zuul.repository.model.DcEnvironment;
import com.zhuxun.dmc.zuul.repository.model.DcRoute;
import com.zhuxun.dmc.zuul.repository.model.DcRouteExample;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * 作用： 动态路由服务
 *
 * <p>时间：2018/6/27 16:26
 *
 * <p>位置：com.zhuxun.dmc.zuul.service
 *
 * @author Yan - tao
 */

@Service
public class DynamicRouteService extends AbstractService {

  /**
   * 新增映射关系规则
   *
   * @param dcRouteList
   * @return
   */
  @Transactional
  public Integer addRouteRule(List<DcRoute> dcRouteList) {
    Preconditions.checkArgument(dcRouteList != null, "更新路由表失败,路由表不能为空");
    int count = 0;
    // 首先删除原数据
    DcRouteExample exampleRoute = new DcRouteExample();
    routeMapper.deleteByExample(exampleRoute);
    for (DcRoute dcRoute : dcRouteList) {
      // 更新数据
      int affect = routeMapper.insert(dcRoute);
      count += affect;
    }
    return count;
  }

  /**
   * 获取当前映射表规则数据
   *
   * @return
   */
  @Transactional(readOnly = true)
  public List<DcRoute> getAllRouteRules() {
    DcRouteExample exampleRouteExample = new DcRouteExample();
    List<DcRoute> dcRoutes = routeMapper.selectByExample(exampleRouteExample);
    return dcRoutes;
  }
}
