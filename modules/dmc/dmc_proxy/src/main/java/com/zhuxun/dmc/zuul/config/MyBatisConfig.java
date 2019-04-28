package com.zhuxun.dmc.zuul.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"com.zhuxun.dmc.zuul.repository.mapper"})
public class MyBatisConfig {
}
