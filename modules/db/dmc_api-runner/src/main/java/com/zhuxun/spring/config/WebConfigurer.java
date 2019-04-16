package com.zhuxun.spring.config;

import com.zhuxun.spring.config.properties.ApplicationProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.EnumSet;

/** Configuration of web application with Servlet 3.0 APIs. */
@Configuration
public class WebConfigurer implements ServletContextInitializer {

  private final Logger log = LoggerFactory.getLogger(WebConfigurer.class);

  private final Environment env;

  private final ApplicationProperties applicationProperties;

  @Autowired
  public WebConfigurer(Environment env, ApplicationProperties applicationProperties) {
    this.env = env;
    this.applicationProperties = applicationProperties;
  }

  @Override
  public void onStartup(ServletContext servletContext) throws ServletException {
    if (env.getActiveProfiles().length != 0) {
      log.info(
          "Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
    }
    EnumSet<DispatcherType> disps =
        EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
    log.info("Web application fully configured");
  }

  @Bean
  public CorsFilter corsFilter() {
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    CorsConfiguration config = applicationProperties.getCors();
    if (config.getAllowedOrigins() != null && !config.getAllowedOrigins().isEmpty()) {
      log.debug("Registering CORS filter");
      source.registerCorsConfiguration("/api/**", config);
      source.registerCorsConfiguration("/management/**", config);
      source.registerCorsConfiguration("/v2/api-docs", config);
    }
    return new CorsFilter(source);
  }
}
