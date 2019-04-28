package com.zhuxun.spring.config;

import com.github.pagehelper.PageHelper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Properties;

/**
 * 作用：暂未说明
 *
 * <p>时间：18-7-12 上午10:27
 *
 * <p>位置：com.zhuxun.spring.config
 *
 * @author Yan - tao
 */
@Configuration
public class PageHelperConfig {

  /**
   * 配置PageHelper插件
   *
   * @return
   */
  @Bean
  public PageHelper pageHelper() {
    PageHelper pageHelper = new PageHelper();
    Properties p = new Properties();
    p.setProperty("offsetAsPageNum", "true");
    p.setProperty("rowBoundsWithCount", "true");
    p.setProperty("reasonable", "true");
    pageHelper.setProperties(p);
    return pageHelper;
  }
}
