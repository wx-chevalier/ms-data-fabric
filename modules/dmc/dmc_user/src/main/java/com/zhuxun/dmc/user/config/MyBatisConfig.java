package com.zhuxun.dmc.user.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"com.zhuxun.dmc.user.repository.mapper"})
public class MyBatisConfig {
}
