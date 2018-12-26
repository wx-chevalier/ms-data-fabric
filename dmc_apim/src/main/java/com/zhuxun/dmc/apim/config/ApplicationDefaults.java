package com.zhuxun.dmc.apim.config;

public interface ApplicationDefaults {
  interface Async {

    int corePoolSize = 2;
    int maxPoolSize = 50;
    int queueCapacity = 10000;
  }

  interface Cache {

    interface Ehcache {

      int timeToLiveSeconds = 3600; // 1 hour
      long maxEntries = 100;
    }
  }

  interface Security {

    interface ClientAuthorization {

      String accessTokenUri = null;
      String tokenServiceId = null;
      String clientId = null;
      String clientSecret = null;
    }

    interface Authentication {

      interface Jwt {

        String secret = null;
        long tokenValidityInSeconds = 1800; // 0.5 hour
        long tokenValidityInSecondsForRememberMe = 2592000; // 30 hours;
      }
    }

    interface RememberMe {

      String key = null;
    }
  }

  interface Swagger {

    String title = "Application API";
    String description = "API documentation";
    String version = "0.0.1";
    String termsOfServiceUrl = null;
    String contactName = null;
    String contactUrl = null;
    String contactEmail = null;
    String license = null;
    String licenseUrl = null;
    String defaultIncludePattern = "/api/.*";
    String host = null;
    String[] protocols = {};
  }
}
