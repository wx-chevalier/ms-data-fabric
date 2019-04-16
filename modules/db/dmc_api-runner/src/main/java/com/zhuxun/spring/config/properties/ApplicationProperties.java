package com.zhuxun.spring.config.properties;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.cors.CorsConfiguration;

// @Component
// @ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
@Accessors(chain = true)
@Data
public abstract class ApplicationProperties {

  private final SecurityProperties security = new SecurityProperties();

  private final CorsConfiguration cors = new CorsConfiguration();

  private final SwaggerProperties swagger = new SwaggerProperties();
}
