package com;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import java.io.UnsupportedEncodingException;
import java.util.Random;


@MapperScan({"com.mapper"})
@SpringBootApplication
public class ApiApplication extends SpringBootServletInitializer {

    // 打war是需要重写	main方法
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        // 设置启动类,用于独立tomcat运行的入口
        return builder.sources(ApiApplication.class);

    }

    // 整个项目的启动方法
    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
    }


}

