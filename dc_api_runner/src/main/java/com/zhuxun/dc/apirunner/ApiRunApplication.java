package com.zhuxun.dc.apirunner;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.zhuxun.dc.apirunner")
@MapperScan(value = "com.zhuxun.dc.apirunner.dao.mapper")
@EnableZuulProxy
public class ApiRunApplication {

  public static void main(String[] args) {
    SpringApplication.run(ApiRunApplication.class, args);
  }
}
