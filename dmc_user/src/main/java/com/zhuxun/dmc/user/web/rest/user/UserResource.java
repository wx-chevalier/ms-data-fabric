package com.zhuxun.dmc.user.web.rest.user;

import com.zhuxun.dmc.user.config.UserConstants;
import com.zhuxun.dmc.user.domain.sec.User;
import com.zhuxun.dmc.user.dto.user.UserCreation;
import com.zhuxun.dmc.user.dto.user.UserUpdate;
import com.zhuxun.dmc.user.security.jwt.TokenProvider;
import com.zhuxun.dmc.user.service.UserService;
import com.zhuxun.dmc.user.web.rest.AbstractResource;
import com.zhuxun.dmc.user.web.rest.errors.client.NotAuthorizedException;
import com.zhuxun.dmc.user.web.rest.errors.client.NotFoundException;
import com.zhuxun.dmc.user.web.rest.errors.server.ServerErrorException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

@RestController
@RequestMapping("/user")
@Slf4j
@Api(value = "/user", description = "用户相关接口")
public class UserResource extends AbstractResource {
  private UserService userService;

  public UserResource(TokenProvider tokenProvider,
                      UserService userService) {
    super(tokenProvider);
    this.userService = userService;
  }

  @GetMapping
  @PreAuthorize("hasRole('MANAGER') or hasRole('ADMIN')")
  public Collection<User> getUserList() {
    return userService.getUserList();
  }

  @ApiOperation(value = "通过用户 id 查找用户信息")
  @GetMapping("/{id}")
  @PreAuthorize("hasAnyRole('MANAGER', 'CLIENT', 'ADMIN')")
  public User getUser(@PathVariable("id") String id, HttpServletRequest request) {
    Boolean hasAdminRole = request.isUserInRole("ADMIN");
    Boolean hasClientRole = request.isUserInRole("CLIENT");
    Boolean hasManagerRole = request.isUserInRole("MANAGER");
    Boolean canAccess = false;
    Optional<User> currentUser = currentUser();
    log.debug("Current User{ADMIN={} CLIENT={} MANAGER={} id={}} try to access User{id={}}",
        hasAdminRole, hasClientRole, hasManagerRole, currentUser.map(User::getId).orElse("null"), id);
    if (currentUser.map(u -> id.equals(u.getId())).orElse(false)) {
      // 任何人可以访问自己的信息
      canAccess = true;
    }
    if (hasAdminRole) {
      canAccess = true;
    }
    if (!canAccess && !hasManagerRole) {
      throw new NotAuthorizedException(format("Can't access User{id=%s}", id));
    }

    User user = userService.getUserById(id)
        .orElseThrow(() -> new NotFoundException(format("User{id=%s}", id)));

    log.debug("Found User={}", user);
    if (!canAccess) {
      // hasManagerRole
      // 确保只能获取 CLIENT 用户
      if (!user.getRoles().stream().anyMatch(r -> UserConstants.ROLE_CLIENT.equals(r.getName()))) {
        throw new NotFoundException(format("User{id=%s}", id));
      }
    }
    return user;
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @ApiOperation(value = "新增用户")
  public User createUser(@RequestBody UserCreation userCreation) {
    return userService.createUser(userCreation)
        .orElseThrow(() -> new ServerErrorException("新增用户失败"));
  }

  @ApiOperation(value = "通过用户 id 删除指定用户")
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public void removeUser(@PathVariable("id") String id) {
    userService.deleteUser(id);
  }

  @ApiOperation(value = "更新用户信息")
  @PatchMapping("/{id}")
  public User updateUser(@PathVariable("id") String id, @RequestBody UserUpdate userUpdate, HttpServletRequest request) {
    if (request.isUserInRole(UserConstants.ROLE_ADMIN) ||
        currentUser().map(u -> id.equals(u.getId())).orElse(false)) {
      // 是 ADMIN 或者更新自己
      return userService.updateUser(id, userUpdate)
          .orElseThrow(() -> new NotFoundException(format("User{id=%s}", id)));
    } else {
      throw new NotAuthorizedException(format("Can't access User{id=%s}", id));
    }
  }

  @ApiOperation(value = "获取全部的 CLIENT 信息")
  @GetMapping("/client")
  @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
  public List<User> getClientList() {
    return userService.getClientList(currentUser().map(User::getId).orElse(null));
  }
}
