package com.zhuxun.dc.apirunner.zuulapi;

import com.hazelcast.util.MD5Util;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.zhuxun.dc.apirunner.constant.HttpConstant;
import com.zhuxun.dc.apirunner.entity.TokenEntity;
import com.zhuxun.dc.apirunner.entity.vo.VResult;
import com.zhuxun.dc.apirunner.entity.vo.VToken;
import com.zhuxun.dc.apirunner.utils.JacksonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

/** @author tao */
@Component
@Configuration
@Slf4j
public class CustomZuulFilter extends ZuulFilter {

  @Autowired RouteLocator routeLocator;

  // 当前登录对象
  @Autowired Subject subject;

  private UrlPathHelper urlPathHelper = new UrlPathHelper();

  /**
   * 过滤类型
   *
   * @return
   */
  @Override
  public String filterType() {
    return FilterConstants.PRE_TYPE;
  }

  @Override
  public int filterOrder() {
    return FilterConstants.PRE_DECORATION_FILTER_ORDER;
  }

  /**
   * 是否过滤
   *
   * @return 返回FALSE则不执行过滤操作
   */
  @Override
  public boolean shouldFilter() {
    return true;
  }

  /**
   * 实际的控制操作
   *
   * @return
   */
  @Override
  public Object run() {
    log.info("进入透明代理前置过滤器....");
    RequestContext context = RequestContext.getCurrentContext();
    String requestURI = this.urlPathHelper.getPathWithinApplication(context.getRequest());
    TokenEntity tokenEntity = (TokenEntity) context.getRequest().getAttribute("token");
    if (tokenEntity == null) {
      errorHandle("当前凭证不存在或者无效,请尝试重新获取新的凭证后重试", context);
      return null;
    }
    String currentUserId = tokenEntity.userId();
    context.set("startTime", System.currentTimeMillis());
    Route matchingRoute = routeLocator.getMatchingRoute(requestURI);
    String location = matchingRoute.getLocation();
    // 获取当前URI,用于和Token中携带的apiId校验
    String uri = context.getRequest().getRequestURI();
    // 获取头部中携带的token
    String token = context.getRequest().getHeader(HttpConstant.TURELORE_TOKEN);
    if (token == null) {
      errorHandle("验证失败,Token不能为空", context);
      return null;
    }
    // 解析token
    VToken vToken = VToken.of(token);

    // 获取指定运行环境ID
    String tokenUserId = vToken.getEnvId();

    if (Objects.equals(tokenUserId, currentUserId)) {
      errorHandle("当前登录账户和Token账户不匹配", context);
      return null;
    }
    // 计算MD5
    String md5 = MD5Util.toMD5String(tokenUserId + vToken.getEnvId());
    //
    //    if (!uri.contains(md5)) {
    //      errorHandle("校验失败，请检查API和环境是否匹配", context);
    //      return null;
    //    }

    // 更新Host信息
    // 检测环境

    // 在context中添加参数用于在POST拦截器中统计使用
    // 添加请求开始时间和token信息
    context.set("token", vToken);
    Object originalRequestPath = context.get(FilterConstants.REQUEST_URI_KEY);
    return null;
  }

  public void errorHandle(String messageContent, RequestContext context) {
    log.error("Handle Zuul exception,exception message = {},", messageContent);
    HttpServletResponse response = context.getResponse();
    response.setCharacterEncoding("utf-8");
    response.setContentType("application/json; charset=utf-8");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    context.setSendZuulResponse(false);
    try {
      response.getWriter().write(JacksonUtil.toJSon(new VResult(messageContent)));
    } catch (IOException e) {
      log.error("Zuul write data to printWrite(Object) happen an  IO exception:", e);
      e.printStackTrace();
    }
    context.setResponse(response);
  }
}
