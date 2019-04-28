package com.zhuxun.dmc.user.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.zhuxun.dmc.user.config.properties.ApplicationProperties;
import com.zhuxun.dmc.user.config.properties.SwaggerProperties;
import com.zhuxun.dmc.user.security.jwt.JWTConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
  private SwaggerProperties swaggerProperties;

  @Autowired
  public SwaggerConfiguration(ApplicationProperties applicationProperties) {
    this.swaggerProperties = applicationProperties.getSwagger();
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .protocols(Sets.newHashSet(swaggerProperties.getProtocols()))
        .select()
        .apis(RequestHandlerSelectors.any())
        .paths(PathSelectors.any())
        .build()
        .apiInfo(apiInfo())
        .host(swaggerProperties.getHost())
        .securityContexts(Lists.newArrayList(securityContext()))
        .securitySchemes(Lists.newArrayList(apiKey()));
  }

  @Bean
  public SecurityConfiguration security() {
    return SecurityConfigurationBuilder.builder()
        .additionalQueryStringParams(null)
        .useBasicAuthenticationWithAccessCodeGrant(false)
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title(swaggerProperties.getTitle())
        .description(swaggerProperties.getDescription())
        .version(swaggerProperties.getVersion())
        .contact(new Contact(
            swaggerProperties.getContactName(),
            swaggerProperties.getContactUrl(),
            swaggerProperties.getContactEmail()))
        .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
        .license(swaggerProperties.getLicense())
        .licenseUrl(swaggerProperties.getLicenseUrl())
        .build();
  }

  private ApiKey apiKey() {
    return new ApiKey("apiKey", JWTConfigurer.AUTHORIZATION_HEADER, "header");
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(defaultAuth())
        .forPaths(PathSelectors.any())
        .build();
  }

  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope(
        "global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Collections.singletonList(new SecurityReference("apiKey", authorizationScopes));
  }
}
