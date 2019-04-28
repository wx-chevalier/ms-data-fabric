package com.zhuxun.dmc.apim.web;

import com.zhuxun.dmc.apim.config.properties.ApplicationProperties;
import com.zhuxun.dmc.apim.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {
  @Bean
  public TokenProvider tokenProvider() {
    return new TokenProvider(applicationProperties());
  }

  @Bean
  public ApplicationProperties applicationProperties() {
    return new ApplicationProperties();
  }
}
