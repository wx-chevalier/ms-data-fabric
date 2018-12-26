package com.zhuxun.dc.apirunner.shiro.realm;




import com.zhuxun.dc.apirunner.entity.TokenEntity;
import com.zhuxun.dc.apirunner.exception.NotAuthenticatedException;
import com.zhuxun.dc.apirunner.utils.JwtUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAccount;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.UnauthorizedException;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.Set;
import java.util.stream.Collectors;

import static com.google.common.base.Strings.isNullOrEmpty;
import static com.zhuxun.dc.apirunner.config.SecretConstant.*;
import static java.util.Objects.isNull;

/**
 * Created by lotuc on 14/04/2017.
 */
@Slf4j
public class JwtAuthenticationRealm extends AuthorizingRealm {
  /**
   * 服务器端 JWT 密钥
   */
  @Setter
  private String secret;

  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    TokenEntity tokenEntity = (TokenEntity) principalCollection.getPrimaryPrincipal();
    if (isNull(tokenEntity) || tokenEntity.equals(TokenEntity.EMPTY_TOKEN)) {
      throw new UnauthorizedException("无效的Token");
    }
    Set<String> permissions = tokenEntity.permissions();

    Set<Permission> permissionSet = null;
    if (permissions != null && permissions.size() != 0) {
      permissionSet = permissions.stream()
          .filter(s -> !isNullOrEmpty(s))
          .map(s -> new WildcardPermission(s))
          .collect(Collectors.toSet());
    }
    return new SimpleAccount(
        tokenEntity.username(),
        null,
        getName(),
        tokenEntity.roles(),
        permissionSet);
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
      throws AuthenticationException {

    JwtAuthenticationToken token = (JwtAuthenticationToken) authenticationToken;
    String authToken = token.getAuthToken();
    if (authToken == null) {
      throw new AuthenticationException("Token不能为空");
    }

    TokenEntity tokenEntity;
    try {
      tokenEntity = JwtUtils.parseTokenToAccount(authToken, JWT_SECRET, getName());
    } catch (Exception e) {
      log.warn("无效的Token令牌 = {} ",authToken);
      throw new NotAuthenticatedException("无效的凭证,请尝试重新获取新的凭证后重试");
    }

    if (tokenEntity.isExpired()) {
      log.warn("过期的Token令牌 = {} ",authToken);
      throw new NotAuthenticatedException("凭证已过期,请尝试重新获取新的凭证后重试");
    }

    return new SimpleAccount(tokenEntity, null, getName());
  }

  @Override
  public boolean supports(AuthenticationToken token) {
    return token instanceof JwtAuthenticationToken;
  }

  @Override
  public CredentialsMatcher getCredentialsMatcher() {
    return new CredentialsMatcher() {
      @Override
      public boolean doCredentialsMatch(AuthenticationToken authenticationToken, AuthenticationInfo authenticationInfo) {
        return true;
      }
    };
  }
}
