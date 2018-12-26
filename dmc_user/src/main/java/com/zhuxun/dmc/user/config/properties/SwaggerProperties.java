package com.zhuxun.dmc.user.config.properties;

import com.zhuxun.dmc.user.config.ApplicationDefaults;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
public class SwaggerProperties {

  private String title = ApplicationDefaults.Swagger.title;

  private String description = ApplicationDefaults.Swagger.description;

  private String version = ApplicationDefaults.Swagger.version;

  private String termsOfServiceUrl = ApplicationDefaults.Swagger.termsOfServiceUrl;

  private String contactName = ApplicationDefaults.Swagger.contactName;

  private String contactUrl = ApplicationDefaults.Swagger.contactUrl;

  private String contactEmail = ApplicationDefaults.Swagger.contactEmail;

  private String license = ApplicationDefaults.Swagger.license;

  private String licenseUrl = ApplicationDefaults.Swagger.licenseUrl;

  private String defaultIncludePattern = ApplicationDefaults.Swagger.defaultIncludePattern;

  private String host = ApplicationDefaults.Swagger.host;

  private String[] protocols = ApplicationDefaults.Swagger.protocols;
}
