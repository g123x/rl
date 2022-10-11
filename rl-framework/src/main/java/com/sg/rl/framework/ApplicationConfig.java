package com.sg.rl.framework;

import com.alibaba.druid.spring.boot.autoconfigure.DruidDataSourceAutoConfigure;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableAspectJAutoProxy(exposeProxy = true)
@MapperScan("com.sg.rl.**.mapper")
@ComponentScan(basePackages = {"com.sg.rl"})
@EnableAsync
@EnableTransactionManagement
@EnableAutoConfiguration(exclude={DruidDataSourceAutoConfigure.class,
        DataSourceAutoConfiguration.class/*, RedisAutoConfiguration.class*/})
public class ApplicationConfig {

}