package com.zhuxun.dc.apirunner.zuulapi;

import com.zhuxun.dc.apirunner.config.SystemInterception;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.cloud.netflix.zuul.web.ZuulHandlerMapping;
import org.springframework.context.annotation.Configuration;

/**
 * 配置Zuul的拦截器，
 *
 * @author tao
 */
@Configuration
@RequiredArgsConstructor
public class ZuulHandlerBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter {

  @Autowired private final SystemInterception systemInterception;

  @Override
  public boolean postProcessAfterInstantiation(final Object bean, final String beanName) throws BeansException {

    if (bean instanceof ZuulHandlerMapping) {
      ZuulHandlerMapping zuulHandlerMapping = (ZuulHandlerMapping) bean;
      // 添加拦截器
      zuulHandlerMapping.setInterceptors(systemInterception);
    }

    return super.postProcessAfterInstantiation(bean, beanName);
  }

}