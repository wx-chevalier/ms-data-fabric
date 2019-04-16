package com.zhuxun.dmc.user.web.rest.auth;

import com.zhuxun.dmc.user.domain.auth.BearerToken;
import com.zhuxun.dmc.user.domain.sec.Permission;
import com.zhuxun.dmc.user.domain.sec.Role;
import com.zhuxun.dmc.user.dto.user.UserLogin;
import com.zhuxun.dmc.user.security.jwt.JWTAuthenticationToken;
import com.zhuxun.dmc.user.security.jwt.TokenProvider;
import com.zhuxun.dmc.user.service.UserService;
import com.zhuxun.dmc.user.web.rest.AbstractResource;
import com.zhuxun.dmc.user.web.rest.errors.client.NotAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("auth")
@Slf4j
public class LoginService extends AbstractResource {
  private UserService userService;

  @Autowired
  public LoginService(TokenProvider tokenProvider, UserService userService) {
    super(tokenProvider);
    this.userService = userService;
  }

  @PostMapping("/login")
  public BearerToken login(@RequestBody UserLogin authInfo) throws NotAuthorizedException {
    log.trace("Login with {}", authInfo);

    return userService.checkPassword(authInfo.getUsername(), authInfo.getPassword())
        .map(u -> {
          List<GrantedAuthority> authorities = new ArrayList<>();
          authorities.addAll(u.getRoles().stream()
              .map(Role::getName)
              .map(n -> new SimpleGrantedAuthority("ROLE_" + n))
              .collect(Collectors.toList()));
          authorities.addAll(u.getPermissions().stream()
              .map(Permission::getName)
              .map(SimpleGrantedAuthority::new)
              .collect(Collectors.toList()));
          return new JWTAuthenticationToken(u.getId(), u.getName(), authorities);
        })
        .map(authentication -> tokenProvider.createToken(
            authentication, authInfo.getRememberMe()))
        .map(new BearerToken()::setToken)
        .orElseThrow(() -> new NotAuthorizedException("登陆失败"));
  }
}
