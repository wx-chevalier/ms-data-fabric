package com.zhuxun.dc.apirunner.shiro.filter;



import com.zhuxun.dc.apirunner.shiro.realm.JwtAuthenticationToken;
import com.zhuxun.dc.apirunner.utils.JwtUtils;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by lotuc on 21/04/2017.
 */
@Slf4j
public class ConfigurableStatelssAuthcFilter extends AccessControlFilter {
  /**
   * 登录 URL，TOKEN 验证将跳过该 URL
   */
  @Setter
  private String loginUrl;

  @Setter
  private Boolean disableAuth = false;

  @Setter
  private List<Map> roles;

  @Setter
  private List<Map> permissions;

  @Setter
  private String userId;

  @Setter
  private String username;

  @Setter
  private String secret;

  @Setter
  private Long tokenAliveTime;

  @Override
  protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
    return false;
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
    System.out.println("=================================FUCKED UP!!!!!!!!!!!!");
    if (disableAuth) {
      return true;
    }

    HttpServletRequest req = (HttpServletRequest) request;
    String requestURI = req.getRequestURI();

    // 登录 URL，忽略验证
    if (requestURI.equals(this.loginUrl)) {
      return true;
    }

    Set<String> permissionsSet = permissions
            .stream()
            .map(map -> (String)map.get("permission"))
            .collect(Collectors.toSet());
    Set<String> roleSet = roles
            .stream()
            .map(map -> (String)map.get("role"))
            .collect(Collectors.toSet());

    String authToken = JwtUtils.createAuthToken(
        this.userId,
        this.username,
        roleSet,
        permissionsSet,
        this.secret,
        this.tokenAliveTime);

    JwtAuthenticationToken token = new JwtAuthenticationToken(request.getRemoteHost(), authToken);

    try {
      getSubject(request, response).login(token);
    } catch (Exception e) {
      log.warn("Login failed with token {}", token);
      throw e;
    }
    return true;
  }

  /**
   * 登录失败时默认返回401状态码
   *
   * @param response
   * @throws IOException
   */
  private void onLoginFail(ServletResponse response) throws IOException {
    HttpServletResponse httpResponse = (HttpServletResponse) response;
    httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    httpResponse.getWriter().write("login error");
  }
}
