package com.zhuxun.dc.apirunner.service;

import com.zhuxun.dc.apirunner.dao.mapper.*;
import com.zhuxun.dc.apirunner.dao.mapper.extra.ApiManageMapper;
import com.zhuxun.dc.apirunner.entity.TokenEntity;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.util.Set;

public class BaseService {

  @Value("${jdbc.datasource.config.dir}")
  protected String configDir;

  @Value("${presto.datasource.username}")
  protected String defaultUsername;

  @Value("${presto.datasource.password}")
  protected String defaultPassword;

  @Value("${presto.datasource.url}")
  protected String defaultUrl;

  @Value("${presto.datasource.driver-class-name}")
  protected String defaultClassName;

  @Autowired protected DcApiMapper dcApiMapper;

  @Autowired protected DcApiParamMapper dcApiParamMapper;

  @Autowired protected DcApiResponseMapper dcApiResponseMapper;

  @Autowired protected DcApiImplMapper dcApiImplMapper;

  @Autowired protected ApiManageMapper apiManageMapper;

  @Autowired protected ManagedDatasourceService managedDatasourceService;

  @Autowired protected DcDataSourceMapper dcDataSourceMapper;

  @Autowired protected DcDataSchemaMapper dcDataSchemaMapper;

  @Autowired protected DcDataTableMapper dcDataTableMapper;

  @Autowired protected DcDataColumnMapper dcDataColumnMapper;

  @Autowired DcRouteMapper dcRouteMapper;

  @Autowired DcEnvironmentMapper dcEnvironmentMapper;

  @Autowired protected DcApiStatisticsMapper dcApiStatisticsMapper;

  // 当前登录对象
  @Autowired protected Subject subject;

  public TokenEntity getTokenEntity() {
    if (subject != null) {
      return (TokenEntity) subject.getPrincipal();
    } else {
      return null;
    }
  }

  /**
   * 获取用户的角色列表
   *
   * @return
   */
  public Set<String> getUserRole() {
    TokenEntity tokenEntity = getTokenEntity();
    if (tokenEntity == null) {
      return null;
    } else {
      return tokenEntity.roles();
    }
  }

  /**
   * 获取用户的ID信息
   *
   * @return
   */
  public String getUserId() {
    TokenEntity tokenEntity = getTokenEntity();
    if (tokenEntity == null) {
      return null;
    } else {
      return tokenEntity.userId();
    }
  }
}
