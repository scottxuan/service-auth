package com.service.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * @author : scottxuan
 */
@SpringBootApplication
@EnableEurekaClient
@MapperScan(basePackages = {"com.service.auth.mapper"})
@ComponentScan(basePackages = {"com.scottxuan","com.module.auth","com.service.auth"})
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class,args);
    }
}
