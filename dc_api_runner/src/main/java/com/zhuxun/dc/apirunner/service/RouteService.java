package com.zhuxun.dc.apirunner.service;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.hazelcast.util.MD5Util;
import com.zhuxun.dc.apirunner.constant.RoleConstant;
import com.zhuxun.dc.apirunner.dao.entity.*;
import com.zhuxun.dc.apirunner.entity.vo.VAffect;
import com.zhuxun.dc.apirunner.utils.JacksonUtil;
import com.zhuxun.dc.apirunner.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Slf4j
public class RouteService extends BaseService {

  /**
   * 删除指定api下的路由信息
   *
   * @param apiId
   * @return
   */
  @Transactional
  @RequiresRoles(
    value = {RoleConstant.ADMIN_NAME, RoleConstant.MANAGER_NAME},
    logical = Logical.OR
  )
  public VAffect deleteRouteByApiId(String apiId) {
    // 在更新的时候已经检查了参数,此处不做校验参数是否合法
    DcRouteExample example = new DcRouteExample();
    int affect = dcRouteMapper.deleteByExample(example);
    return VAffect.of(affect);
  }

  /**
   * 新增映射关系
   *
   * @param dcRouteList
   * @return
   */
  @Transactional
  @RequiresRoles(
    value = {RoleConstant.ADMIN_NAME, RoleConstant.MANAGER_NAME},
    logical = Logical.OR
  )
  public VAffect addRoute(List<DcRoute> dcRouteList) {
    Preconditions.checkArgument(dcRouteList != null, "更新路由表失败,路由表不能为空");
    int count = 0;
    // 首先删除原数据
    DcRouteExample exampleRoute = new DcRouteExample();
    dcRouteMapper.deleteByExample(exampleRoute);
    for (DcRoute dcRoute : dcRouteList) {
      // 更新数据
      // dcRoute.setId(UUIDUtils.getUUID());
      int affect = dcRouteMapper.insert(dcRoute);
      count += affect;
    }
    return VAffect.of(count);
  }

  /**
   * 更新指定接口的映射关系
   *
   * @param apiId 指定API的ID
   * @return
   */
  @Transactional
  @RequiresRoles(
    value = {RoleConstant.ADMIN_NAME, RoleConstant.MANAGER_NAME},
    logical = Logical.OR
  )
  public VAffect updateRouteByApiId(String apiId) {
    Preconditions.checkArgument(!Strings.isNullOrEmpty(apiId), "删除路由信息失败,接口ID不能为空");
    deleteRouteByApiId(apiId);
    // 从接口表中获取接口列表
    DcApiKey key = new DcApiKey();
    key.setId(apiId);
    DcApi dcApi = dcApiMapper.selectByPrimaryKey(key);
    // 组装数据
    DcRoute dcRoute = new DcRoute();
    dcRoute.setPath("/apis/" + apiId + "/**");
    dcRoute.setUrl(dcApi.getApiProtocol() + "://" + dcApi.getApiPath());
    dcRoute.setRetryable(false);
    dcRoute.setStripPrefix(true);
    // refreshRouteService.refreshRoute();
    return addRoute(Arrays.asList(dcRoute));
  }

  /**
   * 更新某个项目下的接口，一般在修改环境变量的时候使用
   *
   * @param projectId
   * @return
   */
  @Transactional
  @RequiresRoles(
    value = {RoleConstant.ADMIN_NAME, RoleConstant.MANAGER_NAME},
    logical = Logical.OR
  )
  public VAffect updateRouteByProjectId(String projectId) {
    return VAffect.of(updateApiRoute().size());
  }

  /**
   * 更新映射表,返回映射的数据信息
   *
   * @return
   */
  @Transactional
  public List<DcRoute> updateApiRoute() {
    // 首先获取所有的环境变量以PROXY_开头
    List<DcEnvironment> proxyNameList = apiManageMapper.getProxyNameList();
    List<DcRoute> dcRouteList = new ArrayList<>();
    for (DcEnvironment environment : proxyNameList) {
      Map readValue = JacksonUtil.readValue(environment.getEnvValue(), Map.class);
      Iterator iterator = readValue.keySet().iterator();
      while (iterator.hasNext()) {
        String key = (String) iterator.next();
        if (key.startsWith("PROXY_")) {
          DcRoute dcRoute = new DcRoute();
          dcRoute.setId(UUIDUtils.getUUID());
          dcRoute.setEnvId(environment.getId());
          dcRoute.setStripPrefix(true);
          dcRoute.setProjectId(environment.getProjectId());
          dcRoute.setRetryable(false);
          dcRoute.setPath(
              "/api/extra/"
                  + MD5Util.toMD5String(environment.getId() + key.replace("PROXY_", ""))
                  + "/**");
          dcRoute.setUrl((String) readValue.get(key));
          dcRouteList.add(dcRoute);
        }
      }
    }
    log.info("从DB中读取到配置{}条,正在更新数据库到代理映射表....", dcRouteList.size());
    addRoute(dcRouteList);
    return dcRouteList;
  }
}
