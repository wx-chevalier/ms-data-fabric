package com.zhuxun.dc.apirunner.shiro.filter;

import com.google.common.base.Strings;
import com.zhuxun.dc.apirunner.shiro.realm.JwtAuthenticationToken;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.AccessControlFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class StatelessAuthcFilter extends AccessControlFilter {
  /**
   * 登录 URL，TOKEN 验证将跳过该 URL
   */
  @Setter
  private String loginUrl;

  @Setter
  private List<String> skipForAuth;

  @Setter
  private Boolean isDev;

  @Getter
  private Map<String, String> devTokens = new HashMap<>();

  @Override
  protected boolean isAccessAllowed(
      ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
    return false;
  }

  public boolean skipURI(String url) {
    if (Strings.isNullOrEmpty(url)) {
      return false;
    }
    if (url.contains("swagger") || url.contains("webjars") || url.contains("/v2/api-docs")) {
      return true;
    }
    return false;
  }

  @Override
  protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
    HttpServletRequest req = (HttpServletRequest) request;
    String requestURI = req.getRequestURI();

    boolean skip = skipURI(requestURI);
    if (skip) {
      return true;
    }

    String authToken;

    if (requestURI.contains("/api/")) {
      return true;
    }

    String header = req.getHeader("Authorization");

    if (header == null || !header.startsWith("Bearer: ")) {
      authToken = null;
    } else {
      authToken = header.substring(8);
      log.debug("AuthToken={}", authToken);
    }

    JwtAuthenticationToken token = new JwtAuthenticationToken(request.getRemoteHost(), authToken);

    try {
      getSubject(request, response).login(token);
    } catch (Exception e) {
      // log.warn("使用token{}登录失败", token);
      // throw new NotAuthenticatedException(e.getMessage());
    }
    return true;
  }
}
