package com.zhuxun.dmc.apim.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = {"com.zhuxun.dmc.apim.repository"})
public class MyBatisConfig {
}
