package com.zhuxun.dc.apirunner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebAppConfigurer extends WebMvcConfigurerAdapter {
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    InterceptorRegistration interceptor = registry.addInterceptor(new SystemInterception());
    interceptor.addPathPatterns("/**");
    interceptor.excludePathPatterns("/swagger-resources/**", "/v2/api-docs", "/swagger-ui.html", "/webjars/**");
    super.addInterceptors(registry);
  }
}
