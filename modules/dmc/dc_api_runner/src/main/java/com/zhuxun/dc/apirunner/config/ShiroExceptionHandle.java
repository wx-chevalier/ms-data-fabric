package com.zhuxun.dc.apirunner.config;

import com.zhuxun.dc.apirunner.exception.NotAuthenticatedException;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/** @author 周涛 */
//@Configuration
//public class ShiroExceptionHandle extends SimpleMappingExceptionResolver {
//  @Override
//  public ModelAndView resolveException(
//      HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
//    System.out.println(ex.getMessage());
//  }
//}
