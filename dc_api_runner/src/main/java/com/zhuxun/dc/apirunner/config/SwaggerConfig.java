package com.zhuxun.dc.apirunner.config;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger.web.SecurityConfiguration;
import springfox.documentation.swagger.web.SecurityConfigurationBuilder;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Swagger接口管理配置
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

  @Value("${swagger.url.prefix}")
  private String swaggerUrlPrefix;

  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .securityContexts(Lists.newArrayList(securityContext()))
        .securitySchemes(Lists.newArrayList(apiKey()))
        .pathMapping(swaggerUrlPrefix == null ? "/" : swaggerUrlPrefix)
        .select()
        // 设置扫描的包名和匹配的注解，添加过滤条件
        .apis(RequestHandlerSelectors.basePackage("com.zhuxun.dc.apirunner"))
        .apis(RequestHandlerSelectors.withClassAnnotation(RestController.class))
        .paths(PathSelectors.any())
        .build();
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
        // 文档内容配置信息
        .title("江苏筑讯数据服务中心项目接口管理系统-接口运行子系统")
        .description("江苏筑讯数据服务中心项目接口管理系统,仅供测试使用")
        .termsOfServiceUrl("http://www.truelore.cn/")
        .version("1.0")
        .build();
  }

  private ApiKey apiKey() {
    return new ApiKey("apiKey", "Authorization", "header");
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

  /**
   * 配置新增参数信息
   *
   * @return
   */
  private List<ApiKey> securitySchemes() {
    return Arrays.asList(new ApiKey("Authorization", "Authorization", "header"));
  }

  private List<SecurityContext> securityContexts() {
    return Arrays.asList(
        SecurityContext.builder()
            .securityReferences(defaultAuth())
            .forPaths(PathSelectors.regex("^(?!login).*$"))
            .build());
  }
}
