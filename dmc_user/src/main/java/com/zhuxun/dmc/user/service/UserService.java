package com.zhuxun.dmc.user.service;

import com.zhuxun.dmc.user.config.UserConstants;
import com.zhuxun.dmc.user.domain.sec.Permission;
import com.zhuxun.dmc.user.domain.sec.Role;
import com.zhuxun.dmc.user.domain.sec.User;
import com.zhuxun.dmc.user.dto.user.UserCreation;
import com.zhuxun.dmc.user.dto.user.UserUpdate;
import com.zhuxun.dmc.user.repository.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.google.common.collect.Iterators.getOnlyElement;

/**
 * TODO 用户名应该保证唯一
 */
@Service
@Slf4j
public class UserService extends AbstractService {
  public List<User> getUserList() {
    return secUserMapper.selectByExample(new SecUserExample())
        .stream()
        .map(User::of)
        .collect(Collectors.toList());
  }

  public List<Role> getUserRoles(String userId) {
    SecUserRoleExample userRoleExample = new SecUserRoleExample();
    userRoleExample.createCriteria().andUserIdEqualTo(userId);
    List<SecUserRole> secUserRoles = secUserRoleMapper.selectByExample(userRoleExample);

    if (secUserRoles.isEmpty()) {
      return new ArrayList<>();
    }

    SecRoleExample secRoleExample = new SecRoleExample();
    secRoleExample.createCriteria()
        .andIdIn(secUserRoles.stream()
            .map(SecUserRole::getRoleId)
            .collect(Collectors.toList()));

    return secRoleMapper.selectByExample(secRoleExample).stream()
        .map(Role::of)
        .collect(Collectors.toList());
  }

  public List<Permission> getUserPermissions(String userId) {
    List<Permission> resultPermissions = new ArrayList<>();

    SecUserPermissionExample userPermissionExample = new SecUserPermissionExample();
    userPermissionExample.createCriteria().andUserIdEqualTo(userId);
    List<SecUserPermission> userPermissions =
        secUserPermissionMapper.selectByExample(userPermissionExample);

    if (!userPermissions.isEmpty()) {
      SecPermissionExample permissionExample = new SecPermissionExample();
      permissionExample.createCriteria().andIdIn(userPermissions.stream()
          .map(SecUserPermission::getPermissionId)
          .collect(Collectors.toList()));
      resultPermissions.addAll(secPermissionMapper
          .selectByExample(permissionExample)
          .stream()
          .map(Permission::of)
          .collect(Collectors.toList()));
    }

    List<Role> roles = getUserRoles(userId);
    if (!roles.isEmpty()) {
      SecRolePermissionExample rolePermissionExample = new SecRolePermissionExample();
      rolePermissionExample.createCriteria()
          .andRoleIdIn(roles.stream()
              .map(Role::getId)
              .collect(Collectors.toList()));
      List<SecRolePermission> rolePermissions =
          secRolePermissionMapper.selectByExample(rolePermissionExample);
      if (!rolePermissions.isEmpty()) {
        SecPermissionExample secPermissionExample = new SecPermissionExample();
        secPermissionExample.createCriteria().andIdIn(rolePermissions.stream()
            .map(SecRolePermission::getPermissionId)
            .collect(Collectors.toList()));
        resultPermissions.addAll(secPermissionMapper
            .selectByExample(secPermissionExample)
            .stream()
            .map(Permission::of)
            .collect(Collectors.toList()));
      }
    }

    return resultPermissions;
  }

  public Optional<User> getUserById(String id) {
    SecUserExample userExample = new SecUserExample();
    userExample.createCriteria().andIdEqualTo(id).andStatusEqualTo(true);
    return Optional.ofNullable(getOnlyElement(secUserMapper.selectByExample(userExample).iterator(), null))
        .map(User::of)
        .map(u -> u
            .setPermissions(this.getUserPermissions(id))
            .setRoles(this.getUserRoles(id)));
  }

  @Transactional
  public Optional<User> createUser(UserCreation userCreation) {
    String userId = UUID.randomUUID().toString();
    secUserMapper.insertSelective(
        userCreation.toEntity(userId));
    return getUserById(userId);
  }

  @Transactional
  public Optional<User> updateUser(String id, UserUpdate userUpdate) {
    secUserMapper.updateByPrimaryKeySelective(userUpdate.toEntity(id));
    return getUserById(id);
  }

  @Transactional
  public void deleteUser(String id) {
    // WARN 逻辑删除
    secUserMapper.updateByPrimaryKeySelective(
        new SecUser().withId(id).withStatus(false));
  }

  /**
   * TODO 用户组管理，根据 managerUserId 筛选
   *
   * @param managerUserId 管理人员用户 ID
   * @return 受管理人员管理的客户用户列表
   */
  @Transactional
  public List<User> getClientList(String managerUserId) {
    SecRoleExample roleExample = new SecRoleExample();
    roleExample.createCriteria().andRoleEqualTo(UserConstants.ROLE_CLIENT);
    List<SecRole> secRoles = secRoleMapper.selectByExample(roleExample);

    if (secRoles.isEmpty()) {
      return new ArrayList<>();
    }

    SecUserRoleExample userRoleExample = new SecUserRoleExample();
    userRoleExample.createCriteria().andRoleIdIn(secRoles.stream()
        .map(SecRole::getId)
        .collect(Collectors.toList()));
    List<SecUserRole> userRoles = secUserRoleMapper.selectByExample(userRoleExample);

    if (userRoles.isEmpty()) {
      return new ArrayList<>();
    }

    SecUserExample userExample = new SecUserExample();
    userExample.createCriteria().andIdIn(userRoles.stream()
        .map(SecUserRole::getUserId)
        .collect(Collectors.toList()));

    return secUserMapper.selectByExample(userExample).stream()
        .map(User::of)
        .collect(Collectors.toList());
  }

  public Optional<User> checkPassword(String username, String password) {
    SecUserExample userExample = new SecUserExample();
    userExample.createCriteria()
        .andUsernameEqualTo(username)
        .andPasswordEqualTo(password);
    return Optional.ofNullable(getOnlyElement(secUserMapper.selectByExample(userExample).iterator(), null))
        .map(SecUser::getId)
        .map(id -> this.getUserById(id).orElse(null));
  }
}
