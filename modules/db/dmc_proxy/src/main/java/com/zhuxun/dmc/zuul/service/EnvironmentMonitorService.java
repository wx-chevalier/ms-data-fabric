package com.zhuxun.dmc.zuul.service;

import com.hazelcast.util.MD5Util;
import com.zhuxun.dmc.zuul.repository.model.*;
import com.zhuxun.dmc.zuul.utils.JacksonUtil;
import com.zhuxun.dmc.zuul.utils.UUIDUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Iterators.getOnlyElement;

/**
 * 作用： 环境监控程序
 *
 * <p>时间：2018/6/27 16:26
 *
 * <p>位置：com.zhuxun.dmc.zuul.service
 *
 * @author Yan - tao
 */
@Service
@Slf4j
public class EnvironmentMonitorService extends AbstractService {

  @Autowired private DynamicRouteService dynamicRouteService;

  /**
   * 获取包含PROXY_的数据列表
   *
   * @return
   */
  @Transactional(readOnly = true)
  public List<DcEnvironment> getEnvironmentList() {
    DcEnvironmentExample environmentExample = new DcEnvironmentExample();
    environmentExample.createCriteria().andEnvValueLike("%PROXY_%");
    List<DcEnvironment> environmentList = environmentMapper.selectByExample(environmentExample);
    return environmentList;
  }

  /**
   * 获取包含PROXY_的数据的数量
   *
   * <p>为了保持性能,不要使用getEnvironmentList().size()
   *
   * @return
   */
  @Transactional(readOnly = true)
  public Long getEnvironmentCount() {
    DcEnvironmentExample environmentExample = new DcEnvironmentExample();
    environmentExample.createCriteria().andEnvValueLike("%PROXY_%");
    Long count = environmentMapper.countByExample(environmentExample);
    return count;
  }

  /**
   * 更新映射表,返回映射的数据信息
   *
   * @return
   */
  @Transactional
  public List<DcRoute> addRouteRules() throws IOException {
    // 首先获取所有的环境变量以PROXY_开头
    List<DcEnvironment> proxyNameList = getEnvironmentList();
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
              "/api/"
                  + MD5Util.toMD5String(environment.getId() + key.replace("PROXY_", ""))
                  + "/**");
          dcRoute.setUrl((String) readValue.get(key));
          dcRouteList.add(dcRoute);
        }
      }
    }
    log.info("从DB中读取到配置{}条,正在更新数据库到代理映射表....", dcRouteList.size());
    dynamicRouteService.addRouteRule(dcRouteList);
    return dcRouteList;
  }
}
