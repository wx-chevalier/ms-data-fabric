package com.zhuxun.dmc.zuul.config.interception;

import com.netflix.zuul.context.RequestContext;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 作用： 配置Zuul的拦截器
 *
 * <p>时间：2018/6/28 08:54</p>
 *
 * <p>位置：com.zhuxun.dmc.zuul.web.zuul</p>
 *
 * @author Yan - tao
 */
@Component
public class TokenCheckInterception extends HandlerInterceptorAdapter {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    // 拦截器发现OPTIONS请求,那么不做任何处理设置头部后立刻返回数据信息
    // OPTIONS请求是跨与请求的预检请求
    if(request.getMethod().equals(HttpMethod.OPTIONS.name())){
      response.setHeader("Access-Control-Allow-Origin", "*");
      response.setHeader("Access-Control-Max-Age", "3628800");
      response.setHeader("Access-Control-Allow-Methods", "*");
      response.setHeader("Access-Control-Allow-Headers", "*");
      return false;
    }
    return true;
  }
}
