package com.zhuxun.dc.apirunner.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceBuilder;
import com.zhuxun.dc.apirunner.sqlapi.impl.SQLAPIAnalyzerImpl;
import com.zhuxun.dc.apirunner.sqlapi.impl.SQLAPIExecutorImpl;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DatasourceConfig {
  /**
   * 引入SQL执行器
   *
   * @return
   */
  @Bean
  public SQLAPIExecutorImpl sqlapiExecutor() {
    SQLAPIExecutorImpl sqlapiExecutor = new SQLAPIExecutorImpl();
    return sqlapiExecutor;
  }

  /**
   * 引入SQL分析器
   *
   * @return
   */
  @Bean
  public SQLAPIAnalyzerImpl sqlapiAnalyzer() {
    SQLAPIAnalyzerImpl sqlapiExecutor = new SQLAPIAnalyzerImpl();
    return sqlapiExecutor;
  }

  @Primary
  @Bean(initMethod = "init", destroyMethod = "close")
  @ConfigurationProperties("spring.datasource")
  public DruidDataSource druidDataSource() {
    return DruidDataSourceBuilder.create().build();
  }
}
