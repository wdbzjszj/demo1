package com.stu8317.demo1;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.stu8317.demo1.mapper") // 扫描Mapper接口
public class Demo1Application {
    public static void main(String[] args) {

        SpringApplication.run(Demo1Application.class, args);
    }
}//