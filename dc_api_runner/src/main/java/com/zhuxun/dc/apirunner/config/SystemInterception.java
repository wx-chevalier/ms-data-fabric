package com.zhuxun.dc.apirunner.config;

import com.google.common.base.Strings;
import com.zhuxun.dc.apirunner.entity.TokenEntity;
import com.zhuxun.dc.apirunner.exception.TokenException;
import com.zhuxun.dc.apirunner.utils.JwtUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.zhuxun.dc.apirunner.config.SecretConstant.JWT_SECRET;

@Component
public class SystemInterception implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    String requestMethod = request.getMethod();
    //如果是浏览器自动发送的跨域验证请求，那么直接放行
    if (requestMethod.equals("OPTIONS")) {
      return true;
    }
    String token = request.getHeader("Authorization");
    if (Strings.isNullOrEmpty(token) || !token.startsWith("Bearer")) {
      throw new TokenException("凭证为空或格式错误");
    }
    TokenEntity tokenEntity = null;
    try {
      tokenEntity = JwtUtils.parseTokenToAccount(token.substring(8), JWT_SECRET, null);
    } catch (Exception e) {
      throw new TokenException("无效的凭证,请尝试重新获取新的凭证后重试");
    }
    if (tokenEntity.isExpired()) {
      throw new TokenException("凭证已过期,请尝试重新获取新的凭证后重试");
    }
    if (Strings.isNullOrEmpty(tokenEntity.userId())) {
      throw new TokenException("凭证解析格式异常,请尝试重新获取凭证后重试");
    }
    request.setAttribute("token", tokenEntity);
    return true;
  }

  @Override
  public void postHandle(
      HttpServletRequest request,
      HttpServletResponse response,
      Object handler,
      ModelAndView modelAndView)
      throws Exception {
  }

  @Override
  public void afterCompletion(
      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
      throws Exception {
  }
}
