package com.zhuxun.dmc.runner.config;

public interface ApplicationConstants {
  String SPRING_PROFILE_DEVELOPMENT = "dev";
  String SPRING_PROFILE_TEST = "test";
  String SPRING_PROFILE_PRODUCTION = "prod";

  //  正常状态
  byte STATUS_NORMAL = 1;
  // 禁用状态
  byte STATUS_DISABLE = 0;
  // 审核中
  byte STATUS_AUDITING = 2;
}
