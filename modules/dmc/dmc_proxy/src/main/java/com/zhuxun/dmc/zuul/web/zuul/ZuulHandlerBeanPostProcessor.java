package com.zhuxun.dmc.zuul.web.zuul;

import com.zhuxun.dmc.zuul.config.interception.TokenCheckInterception;
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

  @Autowired
  private final TokenCheckInterception tokenCheckInterception;

  @Override
  public boolean postProcessAfterInstantiation(final Object bean, final String beanName) throws BeansException {

    if (bean instanceof ZuulHandlerMapping) {
      ZuulHandlerMapping zuulHandlerMapping = (ZuulHandlerMapping) bean;
      // 添加拦截器
      zuulHandlerMapping.setInterceptors(tokenCheckInterception);
    }

    return super.postProcessAfterInstantiation(bean, beanName);
  }

}