package com.example.hlsiidb;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.example.hlsiidb.mybatis")
public class HlsiidbApplication {
    public static void main(String[] args) {
        SpringApplication.run(HlsiidbApplication.class, args);
    }
}
