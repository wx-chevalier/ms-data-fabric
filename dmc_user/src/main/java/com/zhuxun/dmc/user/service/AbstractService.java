package com.zhuxun.dmc.user.service;

import com.zhuxun.dmc.user.repository.mapper.*;
import com.zhuxun.dmc.user.repository.model.SecRolePermission;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractService {
  @Autowired
  protected SecUserMapper secUserMapper;

  @Autowired
  protected SecPermissionMapper secPermissionMapper;

  @Autowired
  protected SecRoleMapper secRoleMapper;

  @Autowired
  protected SecUserRoleMapper secUserRoleMapper;

  @Autowired
  protected SecUserPermissionMapper secUserPermissionMapper;

  @Autowired
  protected SecRolePermissionMapper secRolePermissionMapper;
}
