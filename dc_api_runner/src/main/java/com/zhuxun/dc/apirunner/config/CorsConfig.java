package com.zhuxun.dc.apirunner.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Component
@Configuration
@Slf4j
public class CorsConfig {

  @Value("${cors.allow.origins}")
  private String allowedOrigins;

  @Value("${cors.mapper.url}")
  private String allowedUrl;

  @Bean
  @SuppressWarnings("deprecation")
  public WebMvcConfigurer corsConfigurer() {
    return new WebMvcConfigurerAdapter() {
      @Override
      public void addCorsMappings(CorsRegistry registry) {
        log.info("CORS 配置参数 origins = {}  url = {}", allowedOrigins, allowedUrl);
        registry
            .addMapping(allowedUrl == null ? "/**" : allowedUrl)
            .allowedMethods("GET", "POST", "PATCH", "DELETE", "PUT", "OPTIONS")
            .allowedOrigins(allowedOrigins == null ? "*" : allowedOrigins);
      }
    };
  }
}
