package com.zhuxun.spring.config;

import com.fasterxml.classmate.TypeResolver;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.zhuxun.dmc.apim.domain.api.apiimpl.APIImplDef;
import com.zhuxun.dmc.apim.domain.api.apiimpl.SQLImplDef;
import com.zhuxun.dmc.apim.domain.api.apiimpl.graph.RelGraphImplDef;
import com.zhuxun.spring.config.properties.ApplicationProperties;
import com.zhuxun.spring.config.properties.SwaggerProperties;
import com.zhuxun.spring.security.jwt.JWTConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.paths.RelativePathProvider;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {
  private SwaggerProperties swaggerProperties;

  private ServletContext servletContext;

  @Autowired
  public SwaggerConfiguration(
      ApplicationProperties applicationProperties, ServletContext servletContext) {
    this.swaggerProperties = applicationProperties.getSwagger();
    this.servletContext = servletContext;
  }

  @Bean
  public Docket api() {
    TypeResolver typeResolver = new TypeResolver();
    return new Docket(DocumentationType.SWAGGER_2)
        .protocols(Sets.newHashSet(swaggerProperties.getProtocols()))
        .pathProvider(
            new RelativePathProvider(servletContext) {
              @Override
              protected String applicationPath() {
                return swaggerProperties.getBasePath() + super.applicationPath();
              }
            })
        .additionalModels(
            typeResolver.resolve(APIImplDef.class),
            typeResolver.resolve(SQLImplDef.class),
            typeResolver.resolve(RelGraphImplDef.class))
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
        .contact(
            new Contact(
                swaggerProperties.getContactName(),
                swaggerProperties.getContactUrl(),
                swaggerProperties.getContactEmail()))
        .termsOfServiceUrl(swaggerProperties.getTermsOfServiceUrl())
        .license(swaggerProperties.getLicense())
        .licenseUrl(swaggerProperties.getLicenseUrl())
        .build();
  }

  private ApiKey apiKey() {
    return new ApiKey("apiKey", JWTConfigurer.TRUELORE_KEY, "header");
  }

  private SecurityContext securityContext() {
    return SecurityContext.builder()
        .securityReferences(defaultAuth())
        .forPaths(PathSelectors.any())
        .build();
  }

  private List<SecurityReference> defaultAuth() {
    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
    authorizationScopes[0] = authorizationScope;
    return Collections.singletonList(new SecurityReference("apiKey", authorizationScopes));
  }
}
